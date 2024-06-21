# Gunakan image Python sebagai base
FROM python:3.8-slim

# Menentukan working directory
WORKDIR /app

# Mengcopy requirements.txt ke working directory
COPY requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Mengcopy seluruh aplikasi ke working directory
COPY . .

# Menjalankan aplikasi dengan Gunicorn
CMD ["gunicorn", "--bind", "0.0.0.0:5000", "main:app"]
