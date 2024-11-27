package com.bookstore.service;

import com.bookstore.model.User;
import com.bookstore.model.PurchaseItem;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookRecommendationService {

    /**
     * Recommends books for a user based on similar users' purchase history.
     * Uses Jaccard similarity to find similar users.
     * @param currentUser the user to get recommendations for
     * @param allUsers list of all users in the system
     * @param numRecommendations maximum number of recommendations to return
     * @return list of recommended book IDs
     */
    public List<String> getRecommendedBooks(User currentUser, List<User> allUsers, int numRecommendations) {
        Set<String> currentUserBooks = getUserPurchasedBooks(currentUser);
        
        // Calculate similarity scores with other users
        List<UserSimilarity> similarUsers = new ArrayList<>();
        for (User otherUser : allUsers) {
            if (!otherUser.getId().equals(currentUser.getId())) {
                Set<String> otherUserBooks = getUserPurchasedBooks(otherUser);
                double similarity = calculateJaccardSimilarity(currentUserBooks, otherUserBooks);
                similarUsers.add(new UserSimilarity(otherUser, similarity));
            }
        }
        
        // Sort users by similarity score
        similarUsers.sort((a, b) -> Double.compare(b.similarity(), a.similarity()));
        
        // Collect books from similar users that current user hasn't purchased
        Set<String> recommendations = new HashSet<>();
        for (UserSimilarity similarUser : similarUsers) {
            if (recommendations.size() >= numRecommendations) {
                break;
            }
            Set<String> similarUserBooks = getUserPurchasedBooks(similarUser.user());
            similarUserBooks.removeAll(currentUserBooks); // Remove books current user already has
            recommendations.addAll(similarUserBooks);
        }
        return recommendations.stream()
                .limit(numRecommendations)
                .collect(Collectors.toList());
    }
    
    /**
     * Calculates Jaccard similarity between two sets of books.
     * Jaccard similarity = size of intersection / size of union
     */
    private double calculateJaccardSimilarity(Set<String> set1, Set<String> set2) {
        if (set1.isEmpty() && set2.isEmpty()) {
            return 0.0;
        }
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        
        return (double) intersection.size() / union.size();
    }
    
    /**
     * Gets all book IDs that a user has purchased.
     * @param user
     * @return set of book ID's (as String)
     */
    private Set<String> getUserPurchasedBooks(User user) {
        return user.getPurchases().stream()
                .flatMap(checkout -> checkout.getItems().stream())
                .map(PurchaseItem::getBookId)  // Assuming getBookId returns String
                .collect(Collectors.toSet());
    }
    
    /**
     * Helper record to store user similarity scores.
     */
    private record UserSimilarity(User user, double similarity) {}
}
