#create function


from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.preprocessing import OneHotEncoder

# Read the CSV files into DataFrames
from io import StringIO
import pandas as pd
import json
def process_csv_content(csv_content,csvString2):
    # Process the CSV content
    print("Received CSV content in Python:")
    print(csv_content)
    print(csvString2)

    # Read CSV content into a DataFrame
    csv_buffer = StringIO(csv_content)
    csv_buffer2 = StringIO(csvString2)
    df = pd.read_csv(csv_buffer)

    df_booking=pd.read_csv(csv_buffer2)

    dataFrame_booking=pd.DataFrame(df_booking)

    dataFrame = pd.DataFrame(df)
    # Fill NaN values in 'Rating' column with 0
    dataFrame['Rating'].fillna(0, inplace=True)

    # Print the processed DataFrame
    print(dataFrame)

    user_id = dataFrame_booking.loc[0, 'UserId']
    user_bookings = dataFrame_booking[dataFrame_booking['UserId'] == user_id]
    item_ids = user_bookings['ItemId'].tolist()

    recommended_items = dataFrame[dataFrame['ID'].isin(item_ids)]

    features = ['Category', 'City', 'Price', 'Rating']
    dataFrame['Combined_Features'] = dataFrame['Category'] + ' ' + dataFrame['City']
    vectorizer = CountVectorizer()

# Fit and transform the 'Combined_Features' column
    feature_vectors = vectorizer.fit_transform(dataFrame['Combined_Features'])

# Compute cosine similarity matrix based on feature vectors
    similarity_matrix = cosine_similarity(feature_vectors)




    def recommend_items(user_id, recommended_items, top_n=5):

        user_booked_items = recommended_items[recommended_items['ID'] == user_id]

            # Check if the user has booked any items
        if user_booked_items.empty:
            print("User has not booked any items.")
            return []

    # Extract user features from booked items
        user_features = user_booked_items[['Category', 'City', 'Price', 'Rating']]

    # Combine user and item features for encoding
        all_features = pd.concat([user_features, df[['Category', 'City']]], ignore_index=True)


    # One-hot encode categorical features
        encoder = OneHotEncoder()
        all_features_encoded = encoder.fit_transform(all_features[['Category', 'City']])

        # Split encoded features back into user and item parts
        user_features_encoded = all_features_encoded[:len(user_features)]
        item_features_encoded = all_features_encoded[len(user_features):]

    # Initialize list to store recommended indices
        recommended_indices = []

    # Iterate over each item index
        for i in range(len(df)):
        # Skip if the item is already booked by the user
          if df.loc[i, 'ID'] == user_id:
            continue

        # Extract item features
          item_features = df.loc[i, ['Category', 'City', 'Price', 'Rating']]

        # Encode item features
          item_features_encoded = encoder.transform([item_features[['Category', 'City']]])

        # Compute similarity between item and user's booked items
          similarity_score = cosine_similarity(item_features_encoded, user_features_encoded).mean()

        # Append index and similarity score to list
          recommended_indices.append((i, similarity_score))

    # Sort recommended indices by similarity score (descending order)
        recommended_indices.sort(key=lambda x: x[1], reverse=True)

    # Get top N recommended indices
        recommended_indices = [item[0] for item in recommended_indices[:top_n]]

        return recommended_indices

    def recommend_items_for_all_users(recommended_items, top_n=5):
        recommended_items_dict = {}

    # Iterate over each unique user ID
        unique_user_ids = recommended_items['ID'].unique()
        for user_id in unique_user_ids:
          recommended_indices = recommend_items(user_id, recommended_items, top_n)
          recommended_items_dict[user_id] = recommended_indices

        return recommended_items_dict
    # Return the processed DataFrame if needed


    recommended_items_dict = recommend_items_for_all_users(recommended_items)
    merged_recommended_items = []
    for user_id, recommended_indices in recommended_items_dict.items():
        # Create a dataframe with user_id and recommended indices
         recommended_df = pd.DataFrame({'ID': [user_id]*len(recommended_indices), 'Recommended_Index': recommended_indices})
         merged_recommended_items.append(recommended_df)

    merged_recommended_items_df = pd.concat(merged_recommended_items, ignore_index=True)



    merged_recommended_items_details_df = pd.merge(merged_recommended_items_df, df, left_on='Recommended_Index', right_index=True)
    # Drop duplicates based on the 'ID' column
    merged_recommended_items_details_df = merged_recommended_items_details_df.drop_duplicates(subset='Recommended_Index')
    print(merged_recommended_items_details_df)

    unique_categories = recommended_items['Category'].unique()
    filtered_recommendations = merged_recommended_items_details_df[merged_recommended_items_details_df['Category'].isin(unique_categories)]
    print(filtered_recommendations)


    recommended_ids = filtered_recommendations['ID_y'].tolist()
    print(recommended_ids)


    return recommended_ids









