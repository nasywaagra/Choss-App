import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
from sklearn.preprocessing import LabelEncoder
from collections import defaultdict
import os 

# Read data from CSV file
df_real = pd.read_csv("E:/Python/recomModel/dataBangkit2.csv")

# Add a sustainability column if not present
if 'SustainabilityRating' not in df_real.columns:
    df_real['SustainabilityRating'] = 0.0  # Default sustainability rating

# Encode categorical features using LabelEncoder
label_encoder_jenis_kelamin_real = LabelEncoder()
df_real['JenisKelamin'] = label_encoder_jenis_kelamin_real.fit_transform(df_real['JenisKelamin'])

# Convert 'MBTI' column to strings before label encoding
df_real['MBTI'] = df_real['MBTI'].astype(str)

label_encoder_MBTI_real = LabelEncoder()
df_real['MBTI'] = label_encoder_MBTI_real.fit_transform(df_real['MBTI'])

label_encoder_atasan_real = LabelEncoder()
df_real['Atasan1'] = label_encoder_atasan_real.fit_transform(df_real['Atasan1'])
df_real['Atasan2'] = label_encoder_atasan_real.fit_transform(df_real['Atasan2'])
df_real['Atasan3'] = label_encoder_atasan_real.fit_transform(df_real['Atasan3'])

label_encoder_bawahan_real = LabelEncoder()
df_real['Bawahan1'] = label_encoder_bawahan_real.fit_transform(df_real['Bawahan1'])
df_real['Bawahan2'] = label_encoder_bawahan_real.fit_transform(df_real['Bawahan2'])

# Encode other categorical features
label_encoder_others_real = LabelEncoder()
df_real['Warna'] = label_encoder_others_real.fit_transform(df_real['Warna'])

# Combine relevant columns into a single text column
df_real['combined_text'] = (
    df_real['JenisKelamin'].astype(str) + ' ' +
    df_real['Umur'].astype(str) + ' ' +
    df_real['MBTI'].astype(str) + ' ' +
    df_real['Atasan1'].astype(str) + ' ' +
    df_real['Atasan2'].astype(str) + ' ' +
    df_real['Atasan3'].astype(str) + ' ' +
    df_real['Bawahan1'].astype(str) + ' ' +
    df_real['Bawahan2'].astype(str) + ' ' +
    df_real['Warna'].astype(str)
)

# Create a TF-IDF matrix to represent the features of each item
tfidf_vectorizer_real = TfidfVectorizer(stop_words='english')
tfidf_matrix_real = tfidf_vectorizer_real.fit_transform(df_real['combined_text'])

# Function to get recommendations based on item similarity
def get_recommendations(item_index, cosine_similarities):
    sim_scores = list(enumerate(cosine_similarities))
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
    sim_scores = sim_scores[1:]  # Exclude the item itself
    return sim_scores

# Function to prompt the user for sustainability ratings
def get_sustainability_ratings(num_recommendations):
    sustainability_ratings = {}
    for i in range(num_recommendations):
        sustainability_rating = float(input(f"Please provide sustainability rating (0-10) for recommendation {i + 1}: "))
        sustainability_ratings[i] = sustainability_rating
    return sustainability_ratings

# Function to recommend items for a new user and save to a new CSV file
def recommend_and_save_to_csv(new_user_data, df, label_encoder_jenis_kelamin, label_encoder_MBTI, label_encoder_atasan, label_encoder_bawahan, label_encoder_others, tfidf_vectorizer, tfidf_matrix, num_recommendations, output_folder="output"):
    # Check if the output folder exists, if not, create it
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)

    if isinstance(new_user_data['JenisKelamin'], int):
        # Convert the encoded 'JenisKelamin' back to string
        new_user_data['JenisKelamin'] = label_encoder_jenis_kelamin.inverse_transform([new_user_data['JenisKelamin']])[0]

    stripped_jenis_kelamin = new_user_data['JenisKelamin'].strip()
    if stripped_jenis_kelamin in label_encoder_jenis_kelamin.classes_:
        new_user_data['JenisKelamin'] = label_encoder_jenis_kelamin.transform([stripped_jenis_kelamin])[0]
        new_user_data['MBTI'] = str(new_user_data['MBTI'])
        label_encoder_MBTI.fit(pd.concat([df['MBTI'].astype(str), pd.Series([new_user_data['MBTI']]).astype(str)]))
        new_user_data['MBTI'] = label_encoder_MBTI.transform([new_user_data['MBTI']])[0]
        
        # Convert 'Umur' to the appropriate range
        umur_range = pd.cut([new_user_data['Umur']], bins=[0, 15, 35, 70], labels=['0-14', '15-35', '35-70'], include_lowest=True)[0]

        new_user_text = (
            str(new_user_data['JenisKelamin']) + ' ' +
            str(umur_range) + ' ' +
            str(new_user_data['MBTI'])
        )

        new_user_tfidf = tfidf_vectorizer.transform([new_user_text])

        cosine_similarities_new_user = linear_kernel(new_user_tfidf, tfidf_matrix).flatten()

        new_user_recommendations = get_recommendations(0, cosine_similarities_new_user)  # Assuming item_index=0 for the first item

        # Collect scores for unique Atasan and Bawahan items
        atasans = defaultdict(list)
        bawahans = defaultdict(list)

        for idx, score in new_user_recommendations:
            atasans[df.iloc[idx]['Atasan1']].append(score)
            atasans[df.iloc[idx]['Atasan2']].append(score)
            atasans[df.iloc[idx]['Atasan3']].append(score)

            bawahans[df.iloc[idx]['Bawahan1']].append(score)
            bawahans[df.iloc[idx]['Bawahan2']].append(score)

        # Calculate the weighted average scores
        weighted_scores_atasan = {k: sum(v) / len(v) for k, v in atasans.items()}
        weighted_scores_bawahan = {k: sum(v) / len(v) for k, v in bawahans.items()}

        # Prepare the data for saving to a new CSV file
        output_data = {
            'Atasan': [],
            'Bawahan': [],
            'Score Atasan': [],
            'Score Bawahan': [],
        }

        for atasan, score in sorted(weighted_scores_atasan.items(), key=lambda x: x[1], reverse=True)[:num_recommendations]:
            output_data['Atasan'].append(label_encoder_atasan.inverse_transform([atasan])[0])
            output_data['Score Atasan'].append(score)

        for bawahan, score in sorted(weighted_scores_bawahan.items(), key=lambda x: x[1], reverse=True)[:num_recommendations]:
            output_data['Bawahan'].append(label_encoder_bawahan.inverse_transform([bawahan])[0])
            output_data['Score Bawahan'].append(score)

        # Create a DataFrame from the output data
        output_df = pd.DataFrame(output_data)

        # Save the recommendations to a new CSV file
        output_file_path = os.path.join(output_folder, f"recommendations_{new_user_data['MBTI']}_{umur_range}_{stripped_jenis_kelamin}.csv")
        output_df.to_csv(output_file_path, index=False)

        print(f"Recommendations saved to: {output_file_path}")
    else:
        print(f"Label 'JenisKelamin': '{stripped_jenis_kelamin}' not present in the training data.")

def recommend_for_new_user(user_data, df, label_encoder_jenis_kelamin, label_encoder_MBTI, label_encoder_atasan, label_encoder_bawahan, label_encoder_others, tfidf_vectorizer, tfidf_matrix, num_recommendations):
    # Check if 'JenisKelamin' is present in the encoder's classes
    stripped_jenis_kelamin = user_data['JenisKelamin'].strip()
    
    if stripped_jenis_kelamin in label_encoder_jenis_kelamin.classes_:
        user_data['JenisKelamin'] = label_encoder_jenis_kelamin.transform([stripped_jenis_kelamin])[0]
        user_data['MBTI'] = str(user_data['MBTI'])
        label_encoder_MBTI.fit(pd.concat([df['MBTI'].astype(str), pd.Series([user_data['MBTI']]).astype(str)]))
        user_data['MBTI'] = label_encoder_MBTI.transform([user_data['MBTI']])[0]
        
        # Convert 'Umur' to the appropriate range
        umur_range = pd.cut([user_data['Umur']], bins=[0, 15, 35, 70], labels=['0-14', '15-35', '35-70'], include_lowest=True)[0]

        # Create a text representation for the new user
        user_text = (
            str(user_data['JenisKelamin']) + ' ' +
            str(umur_range) + ' ' +
            str(user_data['MBTI'])
        )

        # Check if CSV file for the user data already exists
        csv_filename = f"recommendations_{user_data['JenisKelamin']}_{umur_range}_{user_data['MBTI']}.csv"
        csv_filepath = os.path.join("E:/Python/recomModel/recommendations", csv_filename)

        user_recommendations_dummy = None

        # Calculate the TF-IDF vector for the new user
        user_tfidf = tfidf_vectorizer.transform([user_text])

        # Calculate the similarity between the new user and existing items
        cosine_similarities_user = linear_kernel(user_tfidf, tfidf_matrix).flatten()

        # Get recommendations for the new user
        user_recommendations = get_recommendations(0, cosine_similarities_user)  # Assuming item_index=0 for the first item
        
        if os.path.exists(csv_filepath):
            # Load recommendations from CSV file
            recommendations_df = pd.read_csv(csv_filepath)
            print(f"Recommendations loaded from {csv_filename}:")
            # Separate recommendations for Atasan and Bawahan
            atasans_df = recommendations_df[recommendations_df['Item'].str.startswith('Atasan')]
            bawahans_df = recommendations_df[recommendations_df['Item'].str.startswith('Bawahan')]

            # Display top recommendations for Atasan
            print("\nTop Recommendations for Atasan:")
            for _, row in atasans_df.iterrows():
                print(f"{row['Item']}, Score: {row['Score']}")

            # Display top recommendations for Bawahan
            print("\nTop Recommendations for Bawahan:")
            for _, row in bawahans_df.iterrows():
                print(f"{row['Item']}, Score: {row['Score']}")
        else:
            # Collect scores for unique Atasan and Bawahan items
            atasans = defaultdict(list)
            bawahans = defaultdict(list)

            for idx, score in user_recommendations:
                atasans[df.iloc[idx]['Atasan1']].append(score)
                atasans[df.iloc[idx]['Atasan2']].append(score)
                atasans[df.iloc[idx]['Atasan3']].append(score)
                bawahans[df.iloc[idx]['Bawahan1']].append(score)
                bawahans[df.iloc[idx]['Bawahan2']].append(score)

            # Calculate the weighted average scores
            weighted_scores_atasan = {k: sum(v) / len(v) for k, v in atasans.items()}
            weighted_scores_bawahan = {k: sum(v) / len(v) for k, v in bawahans.items()}
            
            # Display the top recommendations for Atasan, Bawahan, and Warna
            print("Top Recommendations for Atasan:")
            for atasan, score in sorted(weighted_scores_atasan.items(), key=lambda x: x[1], reverse=True)[:num_recommendations]:
                print(f"Atasan: {label_encoder_atasan.inverse_transform([atasan])[0]}, Score: {score}")

            print("\nTop Recommendations for Bawahan:")
            for bawahan, score in sorted(weighted_scores_bawahan.items(), key=lambda x: x[1], reverse=True)[:num_recommendations]:
                print(f"Bawahan: {label_encoder_bawahan.inverse_transform([bawahan])[0]}, Score: {score}")

            print(f"\nWarna: {label_encoder_others.inverse_transform([df.iloc[user_recommendations[0][0]]['Warna']])[0]}")

            # Save the recommendations to a new CSV file
            output_folder = "E:/Python/recomModel/recommendations"
            os.makedirs(output_folder, exist_ok=True)
            output_file = os.path.join(output_folder, csv_filename)

            # Prepare data for saving to CSV
            output_data = {'Item': [], 'Score': []}
            for atasan, score in sorted(weighted_scores_atasan.items(), key=lambda x: x[1], reverse=True)[:num_recommendations]:
                output_data['Item'].append(f"Atasan: {label_encoder_atasan.inverse_transform([atasan])[0]}")
                output_data['Score'].append(score)

            for bawahan, score in sorted(weighted_scores_bawahan.items(), key=lambda x: x[1], reverse=True)[:num_recommendations]:
                output_data['Item'].append(f"Bawahan: {label_encoder_bawahan.inverse_transform([bawahan])[0]}")
                output_data['Score'].append(score)

            # Save to CSV
            pd.DataFrame(output_data).to_csv(output_file, index=False)
        
        # Ask the user if they want to provide sustainability ratings
        rate_sustainability = input("Do you want to provide sustainability ratings? (yes/no): ").lower()

        if rate_sustainability == 'yes':
            # Get sustainability ratings from the user
            sustainability_ratings = {}
            i = 0
            while i < num_recommendations:
                try:
                    sustainability_rating = float(input(f"Please provide sustainability rating (0-10) for recommendation {i + 1}: "))
                    if 0 <= sustainability_rating <= 10:
                        sustainability_ratings[i] = sustainability_rating
                        i += 1
                    else:
                        print("Invalid input. Please enter a numeric value between 0 and 10.")
                except ValueError:
                    print("Invalid input. Please enter a numeric value.")

            # Update the recommendation scores based on sustainability ratings
            # Ensure that there are enough sustainability ratings
            if len(sustainability_ratings) != num_recommendations:
                print("Error: Number of sustainability ratings does not match the number of recommendations.")
            else:
                updated_recommendations = []
                for i, (idx, score) in enumerate(user_recommendations):
                    print("Calculate...")
                    sustainability_rating = sustainability_ratings.get(i, 1.0)  # Default to 1.0 if not provided
                    score *= (sustainability_rating * 0.1) + sustainability_rating
                    updated_recommendations.append((idx, score))

                    # Collect scores for unique Atasan and Bawahan items with updated scores
                    atasans = defaultdict(list)
                    bawahans = defaultdict(list)

                    for i, (idx, score) in enumerate(updated_recommendations):
                        if i < len(sustainability_ratings):
                            df.at[idx, 'SustainabilityRating'] = sustainability_ratings[i]
                        atasans[df.iloc[idx]['Atasan1']].append(score)
                        atasans[df.iloc[idx]['Atasan2']].append(score)
                        atasans[df.iloc[idx]['Atasan3']].append(score)
                        bawahans[df.iloc[idx]['Bawahan1']].append(score)
                        bawahans[df.iloc[idx]['Bawahan2']].append(score)

                    # Calculate the weighted average scores with updated scores
                    weighted_scores_atasan = {k: sum(v) / len(v) for k, v in atasans.items()}
                    weighted_scores_bawahan = {k: sum(v) / len(v) for k, v in bawahans.items()}

                # Display the top recommendations for Atasan, Bawahan, and Warna with updated scores
                print("\nTop Recommendations (with updated scores) for Atasan:")
                for i, (atasan, score) in enumerate(sorted(weighted_scores_atasan.items(), key=lambda x: x[1], reverse=True)[:num_recommendations]):
                    print(f"Atasan: {label_encoder_atasan.inverse_transform([atasan])[0]}, Score: {score}")

                print("\nTop Recommendations (with updated scores) for Bawahan:")
                for i, (bawahan, score) in enumerate(sorted(weighted_scores_bawahan.items(), key=lambda x: x[1], reverse=True)[:num_recommendations]):
                    print(f"Bawahan: {label_encoder_bawahan.inverse_transform([bawahan])[0]}, Score: {score}")

                # Save the updated recommendations to a new CSV file
                output_folder = "E:/Python/recomModel/recommendations"
                os.makedirs(output_folder, exist_ok=True)
                output_file = os.path.join(output_folder, csv_filename)

                # Prepare data for saving to CSV
                output_data = {'Item': [], 'Score': []}
                for atasan, score in sorted(weighted_scores_atasan.items(), key=lambda x: x[1], reverse=True)[:num_recommendations]:
                    output_data['Item'].append(f"Atasan: {label_encoder_atasan.inverse_transform([atasan])[0]}")
                    output_data['Score'].append(score)

                for bawahan, score in sorted(weighted_scores_bawahan.items(), key=lambda x: x[1], reverse=True)[:num_recommendations]:
                    output_data['Item'].append(f"Bawahan: {label_encoder_bawahan.inverse_transform([bawahan])[0]}")
                    output_data['Score'].append(score)
                    
                print(f"Recommendations saved to: {output_file}")
                # Save to CSV
                pd.DataFrame(output_data).to_csv(output_file, index=False)
    else:
        print(f"Label 'JenisKelamin': '{stripped_jenis_kelamin}' not present in the training data.")

if __name__ == '__main__':
    # Example of recommending items for a new user
    # Iterate over rows in the input CSV file and generate recommendations

    num_recommendations_real=3

    # for index, row in df_real.iterrows():
    #     user_data = {
    #         'JenisKelamin': row['JenisKelamin'],
    #         'Umur': row['Umur'],
    #         'MBTI': row['MBTI']
    #     }

    #     recommend_and_save_to_csv(user_data, df_real, label_encoder_jenis_kelamin_real,
    #                             label_encoder_MBTI_real, label_encoder_atasan_real,
    #                             label_encoder_bawahan_real, label_encoder_others_real,
    #                             tfidf_vectorizer_real, tfidf_matrix_real, num_recommendations_real)
    
    print("SAVE THE DATA SUCCESS - N-USER CHECK\n")
    
    # Example of recommending items for a new user
    new_user_data = {
        'JenisKelamin': 'Laki - Laki',
        'Umur': 17,
        'MBTI': 'ENFJ'
    }

    recommend_for_new_user(new_user_data, df_real, label_encoder_jenis_kelamin_real, 
                           label_encoder_MBTI_real, label_encoder_atasan_real, 
                           label_encoder_bawahan_real, label_encoder_others_real, 
                           tfidf_vectorizer_real, tfidf_matrix_real, num_recommendations_real)
