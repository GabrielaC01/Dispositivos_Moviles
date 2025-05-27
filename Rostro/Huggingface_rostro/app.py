from fastapi import FastAPI, File, UploadFile, HTTPException, Query
from fastapi.responses import HTMLResponse
from pydantic import BaseModel
from typing import List
import cv2
from PIL import Image
import numpy as np
from io import BytesIO

app = FastAPI()

def buscar_existe(image):
    existe = "NO"
    print("resultado: ", image.shape)
    eyeglasses_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml') 
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    eyeglasses = eyeglasses_cascade.detectMultiScale(gray, 1.3, 5, minSize=(10, 10))
    for (x, y, w, h) in eyeglasses:
        existe = "SI"
        break

    return existe

# Ruta de predicción
@app.post('/predict/')
async def predict(file: UploadFile = File(...), rostro: str = Query(...)):
    try:
        image = Image.open(BytesIO(await file.read()))
        image = np.asarray(image)
        prediction = buscar_existe(image)
        return {"prediction": prediction}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))