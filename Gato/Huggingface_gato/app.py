from fastapi import FastAPI, File, UploadFile, HTTPException, Query
import cv2
from PIL import Image
import numpy as np
from io import BytesIO
import base64

app = FastAPI()

# Carga del clasificador Haar para detectar gatos
gato_cascade = cv2.CascadeClassifier('haarcascade_frontalcatface.xml')

def buscar_y_dibujar(image):
    try:
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

        # Detectar objetos (gatos)
        detecciones = gato_cascade.detectMultiScale(gray, 1.3, 5, minSize=(10, 10))
        existe = "SI" if len(detecciones) > 0 else "NO"

        # Dibujar rectángulos sobre los objetos detectados
        for (x, y, w, h) in detecciones:
            cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)

        # Codificar imagen con rectángulos a JPG
        success, buffer = cv2.imencode('.jpg', image)
        if not success:
            raise ValueError("No se pudo codificar la imagen a JPG")

        # Convertir a base64
        base64_image = base64.b64encode(buffer).decode('utf-8')
        return existe, base64_image

    except Exception as e:
        print("Error interno en buscar_y_dibujar:", str(e))
        raise

@app.post('/predict/')
async def predict(file: UploadFile = File(...), objeto: str = Query(...)):
    try:
        # Leer imagen enviada desde la app
        contents = await file.read()
        image = Image.open(BytesIO(contents)).convert("RGB")
        image = np.array(image).copy()

        # Procesar imagen
        prediction, img_base64 = buscar_y_dibujar(image)

        print("Predicción:", prediction)

        # Responder a la app
        return {
            "prediction": prediction,
            "image": img_base64
        }

    except Exception as e:
        print("ERROR:", str(e))
        raise HTTPException(status_code=500, detail=str(e))
