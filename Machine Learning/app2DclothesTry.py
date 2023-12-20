import cv2
import cvzone
from cvzone.PoseModule import PoseDetector

import numpy as np
import os

from PIL import Image

# Example usage
shirtFolder = "E:/Python/libClothes2d/Atasan"
pantsFolder = "E:/Python/libClothes2d/Bawahan"
outputFolder = "E:/Python/libClothes2d/resized_images"
outputFolder_mask = "E:/Python/libClothes2d/masked_images"
target_size = (1024, 768)

listshirt = os.listdir(shirtFolder)
listpants = os.listdir(pantsFolder)

fixedRatio = 262/190
shirtRatioWidthHeight = 581/440

def resize_and_remove_background(input_path, output_path, output_path_mask, target_size):
    img = cv2.imread(input_path)
    
    if img is not None:
        # Convert the image to grayscale
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

        # Apply thresholding to create a mask (assuming background is white)
        _, mask = cv2.threshold(gray, 300, 255, cv2.THRESH_BINARY)
        _, mask1 = cv2.threshold(gray, 255, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)

        # cv2.imwrite(output_path_mask, mask1)

        # Invert the mask
        mask_inv = cv2.bitwise_not(mask)
        mask_inv1 = cv2.bitwise_not(mask1)
        cv2.imwrite(output_path_mask, mask_inv1)

        # Apply the mask to the original image
        result = cv2.bitwise_and(img, img, mask=mask_inv)

        # Resize the image while maintaining its aspect ratio
        height, width = result.shape[:2]
        aspect_ratio = width / height
        new_width = int(target_size * aspect_ratio)
        resized_img = cv2.resize(result, (new_width, target_size))

        # Save the resized and background-removed image
        cv2.imwrite(output_path, resized_img)
        print(f"Resized and background removed: {output_path}")
    else:
        print(f"Error: Could not read image from {input_path}")

def process_images_in_folders(folders, output_folder, target_size):

# libClothes2d/
# |-- Atasan/
# |   |-- AtasanBerkerah/
# |   |   |-- 1.png
# |   |   |-- 2.png
# |   |   |-- 3.png
# |   |-- T-shirt/
# |       |-- 1.png
# |       |-- 2.png
# |       |-- 3.png
# |-- Bawahan/
#     |-- ...

    print("folder access")

    # Create the output folder if it doesn't exist
    os.makedirs(output_folder, exist_ok=True)

    # Iterate through clothing categories (Atasan, Bawahan)
    for category_folder in [shirtFolder, pantsFolder]:
        # Iterate through subfolders (AtasanBerkerah, T-shirt, etc.)
        for subfolder in os.listdir(category_folder):
            subfolder_path = os.path.join(category_folder, subfolder)
            if os.path.isdir(subfolder_path):
                # Iterate through numbered images (1.png, 2.png, 3.png)
                for filename in os.listdir(subfolder_path):
                    # print("cekkk")
                    if filename.lower().endswith((".png", ".jpg")):
                        img_path = os.path.join(subfolder_path, filename)
                    
                        # Define the output path for the resized image
                        output_path = os.path.join(output_folder, f"{subfolder}_{filename}")
                        output_path_mask = os.path.join(outputFolder_mask, f"{subfolder}_{filename}")

                        # print("masuk resize")
                        # Resize and center the image
                        resize_and_remove_background(img_path, output_path, output_path_mask, target_size=100)  # Adjust the target_size
    print("done")

def change_clothing_color(image_path, new_color):
    # Baca gambar
    img = cv2.imread(image_path)

    # Ubah ke ruang warna HSV
    hsv_img = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)

    # Tentukan batas nilai untuk warna putih pada gambar
    lower_white = np.array([0, 0, 200])
    upper_white = np.array([255, 30, 255])

    # Buat mask untuk warna putih
    mask_white = cv2.inRange(hsv_img, lower_white, upper_white)

    # Invert mask untuk mendapatkan area yang bukan warna putih
    mask_clothing = cv2.bitwise_not(mask_white)

    # Ekstrak area baju
    clothing = cv2.bitwise_and(img, img, mask=mask_clothing)

    # Ganti warna baju dengan warna biru
    clothing[:, :, 0] = 0  # Saluran biru
    clothing[:, :, 1] = 1  # Saluran hijau
    clothing[:, :, 2] = 255  # Saluran merah

    # Gabungkan kembali dengan area lain yang bukan warna putih
    result = cv2.addWeighted(img, 1, clothing, 1, 0)

    # Tampilkan gambar asli dan hasilnya
    cv2.imshow('Original Image', cv2.imread(image_path))
    cv2.imshow('Modified Image', result)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

def pose_detection(): 
    print("POSE DETECTION")

    cap = cv2.VideoCapture(0)
    # Initialize the PoseDetector class with the given parameters
    detector = PoseDetector(staticMode=False,
                            modelComplexity=1,
                            smoothLandmarks=True,
                            enableSegmentation=False,
                            smoothSegmentation=True,
                            detectionCon=0.5,
                            trackCon=0.5)

    while True:
        success, img = cap.read()

        # Find the human pose in the frame
        img = detector.findPose(img)
        # img = cv2.flip(img,1)

        # Find the landmarks, bounding box, and center of the body in the frame
        # Set draw=True to draw the landmarks and bounding box on the image
        lmList, bboxInfo = detector.findPosition(img, draw=False, bboxWithHands=False)

        # Check if any body landmarks are detected2
        if lmList:
            # Get the center of the bounding box around the body
            center = bboxInfo["center"]

            # Draw a circle at the center of the bounding box
            cv2.circle(img, center, 5, (255, 0, 255), cv2.FILLED)

            # print(lmList)

            # FOR CLOTHES 
            lm11 = lmList[11][1:3]
            lm12 = lmList[12][1:3]

            # FOR PANTS
            cm11 = lmList[11][1:3]
            cm12 = lmList[12][1:3]

            # imgshirt = cv2.imread(os.path.join(shirtFolder,listshirt[0]),cv2.IMREAD_UNCHANGED)     
            # imgpants = cv2.imread(os.path.join(shirtFolder,listshirt[0]),cv2.IMREAD_UNCHANGED)     

            # widthofShirt = int((lm11[0]-lm12[0])*fixedRatio)
            # widthofPant = int((cm11[0]-cm12[0])*fixedRatio)

            # if widthofShirt > 0:
            #     imgshirt = cv2.resize(imgshirt,(widthofShirt, int(widthofShirt*shirtRatioWidthHeight)))   
            # if widthofPant > 0:
            #     imgpant = cv2.resize(imgpants,(widthofPant, int(widthofPant*shirtRatioWidthHeight)))   

            # currentScaleS = (lm11[0]-lm12[0]) / 190
            # offsetS = int(44 * currentScaleS), int(48 * currentScaleS)

            # currentScaleP = (cm11[0]-cm12[0]) / 190
            # offsetP = int(44 * currentScaleP), int(48 * currentScaleP)

            # try:
            #     img = cvzone.overlayPNG(img,imgshirt,(lm12[0]-offsetS[0], lm12[1]-offsetS[1]))
            #     img = cvzone.overlayPNG(img,imgpant,(cm12[0]-offsetP[0], cm12[1]-offsetP[1]))

            # except:
            #     pass

        cv2.imshow("Video",img)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        

if __name__ == '__main__':

    new_color = [0, 0, 255]  # Warna biru (BGR format)

    # List of folders to process
    folders_to_process = [shirtFolder, pantsFolder]

    process_images_in_folders(folders_to_process, outputFolder, target_size)

    shirt_use_path = "E:/Python/libClothes2d/resized_images/AtasanBerkerah_2.jpg"
    pant_use_path = "E:/Python/libClothes2d/Bawahan/"

    change_clothing_color(shirt_use_path, new_color)

    # pose_detection()

