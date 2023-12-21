<h1 align="center">
  ChoSS ( Choose Your Style Synchronization )
</h1>
<p align="justify">
A lack of self-awareness about our fashion needs can depend on many situations, one of which is the uniqueness we have, 
namely aesthetics, personality, unique style, and latest fashion trends. As for Google, it's easy to find
information online, but can Google determine and recommend fashion choices that suit our
aesthetics, personality, and align with current fashion trends? This is where our team introduces an
application as a solution to address this dilemma. With our app, there's no need to spend endless
hours searching for fashion that complements your individuality, as it helps identify and recommend
fashion items that align with your unique style, personality, and the latest fashion trends. By
developing the Smart Fashion Advisor App, we aim to empower individuals to effortlessly discover
fashion that truly resonates with their authentic experience. We want to make a style of shopping that
is more efficient, enjoyable, and personal. Our goal is to eliminate the time-consuming or spend more
time like hours or more and disappointing process of searching for the perfect outfit. Beside that, we also
want to make the user more confident with their style based on their MBTI.
</p>

## Requirement

- Python [python.org](https://www.python.org/downloads/)
- OpenCV
```
pip install opencv-python
``` 
- Android Studio [developer.android.com](https://developer.android.com/studio).
- Visual Studicode [code.visualstudio.com](https://code.visualstudio.com/download).
- Figma [figma.com](https://www.figma.com/)

## How to Implement Our Code

### Step 1: Download Recommendation Fashion Model and Dataset

- Download our Recommendation Fashion Model: [RecomFashion.py](https://github.com/nasywaagra/Choss-App/blob/master/Machine%20Learning/RecomFashion.py)
- Download the dataset: [fashion_dataset_updated.xslx](https://github.com/nasywaagra/Choss-App/blob/master/Machine%20Learning/fashion_dataset_updated.xlsx)

### Step 2: Design UI/UX Model using Figma

- Design your UI/UX model using Figma for a visually appealing user interface.

### Step 3: Implement Code using Android Studio with Kotlin

- Implement the UI/UX design using Android Studio and the Kotlin programming language.

### Step 4: Deploy Model from Python to Application

- Deploy the Recommendation Fashion Model from Python to the application using the following steps:
  1. Open [RecomFashion.py](https://github.com/nasywaagra/Choss-App/blob/master/Machine%20Learning/RecomFashion.py) in your Python environment.
  2. Load the dataset [fashion_dataset_updated.xslx](https://github.com/nasywaagra/Choss-App/blob/master/Machine%20Learning/fashion_dataset_updated.xlsx) into the model.
  3. Train the model to provide accurate fashion recommendations.https://drive.google.com/file/d/1FG0Xa4i1HNfyxIlReJsA9b1vaOrCZMUR/view?usp=drive_link
  4. Save the trained model.

### Step 5: Deploy Using API

- Set up an API to connect the trained model to the Android application. Here's a simple example using Flask:

```python
# Sample Flask API code (app.py)

from flask import Flask, request, jsonify
import RecomFashion  # Import your Recommendation Fashion Model

app = Flask(__name__)

@app.route('/recommend', methods=['POST'])
def recommend_fashion():
    data = request.json  # Input data from the Android application
    # Use the trained model to provide fashion recommendations based on input data
    recommended_items = RecomFashion.predict(data)
    return jsonify({'recommendations': recommended_items})

if __name__ == '__main__':
    app.run(debug=True)
```

## Features

- **MBTI-Based Recommendations:** Discover fashion suggestions tailored to your unique personality type.
- **Tops and Bottoms Styling:** Get personalized recommendations for both tops and bottoms.
- **Free and Accessible:** FashionInsight is completely free to use, ensuring that everyone can access personalized style advice.
- **Anywhere, Anytime:** Enjoy the flexibility of using our app anytime, anywhere, without restrictions on time or location
- **Shop Recommendation:** Provides and displays several online store recommendations for purchasing clothing item recommendations

## Future Feature

- Integration with social media platforms for sharing fashion choices.
- Real-time fashion trend updates.
- Personalized fashion advice based on user feedback.
- In-app purchases for suggested fashion items.
- Integration with e-commerce platforms for seamless shopping experience.
- Advanced Personalization Algorithms
- Social Collaboration
- Expert Fashion Insights
  

## Project Status

ChoSS is an actively developed project with a focus on continuous improvement and innovation. We welcome contributions from the open-source community and value feedback from our users. Our development roadmap includes exciting features and enhancements to provide an even more immersive fashion experience.

### Current Milestones

- **Version 1.0 (Latest Release):**
  - MBTI-based recommendations for tops and bottoms.
  - Free and accessible to all users.
  - Anytime, anywhere access.
  - Try-on Virtual
  - Online Shop Recommendation
