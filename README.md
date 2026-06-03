# 🎬 AI-Powered Subtitle Generator & Translator

An end-to-end cloud-integrated subtitle generation system that automatically transcribes audio/video files, translates subtitles into English, generates downloadable SRT files, and stores subtitle metadata using Microsoft Azure cloud services.

Built using **Java Servlets, JSP, FastAPI, Faster-Whisper, FFmpeg, Azure AI Translator and Azure PostgreSQL**.

---

## 📖 Project Overview

The Subtitle Generator System automates the process of generating subtitles from multimedia content.

Users upload an audio or video file through a web interface and select the source language. The system extracts audio, performs Automatic Speech Recognition (ASR), translates subtitles into English, generates downloadable subtitle files, and stores subtitle metadata for future analysis.

This project demonstrates the integration of:

* Java EE Web Technologies
* Python-based AI Services
* Cloud Computing
* REST APIs
* Database Systems
* Multimedia Processing

---

## 🚀 Key Features

### 🎙️ Automatic Speech Recognition

* Speech-to-text conversion using Faster-Whisper
* Multilingual transcription support
* Timestamped subtitle generation

### 🌍 Subtitle Translation

* Translation using Azure AI Translator
* Source language to English conversion
* Preserves subtitle structure and timing

### 🎥 Audio & Video Processing

* Audio and video file upload support
* Audio extraction using FFmpeg
* Audio preprocessing and normalization

### 📄 Subtitle Generation

* Automatic subtitle segmentation
* Downloadable `.srt` subtitle files
* Original and translated subtitle views

### ☁️ Azure Cloud Integration

* Azure Virtual Machine
* Azure AI Translator
* Azure PostgreSQL Flexible Server

### 📊 Data Storage & Analytics

Stores:

* File metadata
* Original subtitles
* Translated subtitles
* Processing timestamps
* Media information

for future analytics and monitoring.

---

## 🏗️ System Architecture

```text
User
 │
 ▼
JSP Web Interface
 │
 ▼
Java Servlet (ASRClient)
 │
 ├── File Upload Handling
 ├── REST API Communication
 │
 ▼
FFmpeg
 │
 └── Audio Extraction
 │
 ▼
Python FastAPI Server (Azure VM)
 │
 └── Faster-Whisper ASR
 │
 ▼
Source Language Subtitles (JSON)
 │
 ▼
Azure AI Translator
 │
 ▼
English Subtitles
 │
 ▼
Java Web Application
 │
 ├── Subtitle Rendering
 ├── SRT Generation
 │
 ▼
Azure PostgreSQL Database
 │
 └── Metadata Storage

```

---

## 🔄 Project Workflow

1. User uploads an audio/video file.
2. User selects the source language.
3. Java Servlet receives the request.
4. FFmpeg extracts and preprocesses audio.
5. Audio is sent to the FastAPI server.
6. Faster-Whisper performs speech recognition.
7. JSON transcription data is returned.
8. Azure Translator converts subtitles into English.
9. Subtitles are displayed on the website.
10. User downloads subtitles in `.srt` format.
11. Metadata and subtitles are stored in Azure PostgreSQL.

---

## ✨ Novelty & Contribution

Unlike traditional subtitle generators, this project:

* Integrates Java EE, Python AI services, and Azure cloud technologies.
* Uses a distributed architecture with separate ASR and web-processing layers.
* Combines speech recognition, translation, subtitle generation, and cloud storage.
* Maintains both source-language and translated subtitles.
* Stores subtitle data for future analytics and monitoring.
* Demonstrates real-world cloud deployment using Azure services.

---

## 🛠️ Technology Stack

### Frontend

* JSP
* HTML5
* CSS3
* JavaScript
* AJAX

### Backend

* Java Servlets
* Java EE

### AI / Machine Learning

* Faster-Whisper
* Automatic Speech Recognition (ASR)

### Python Services

* FastAPI
* Uvicorn

### Cloud Services

* Azure Virtual Machine
* Azure AI Translator
* Azure PostgreSQL Flexible Server

### Media Processing

* FFmpeg

### Database

* PostgreSQL

### Development Tools

* Eclipse IDE
* Visual Studio Code

---

## 📂 Repository Structure

```text
ASRWebApp/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── ASRClient.java
│   │   │   ├── SubtitleDBService.java
│   │   │   └── TestPostgresConnection.java
│   │   │
│   │   └── webapp/
│   │       ├── index.jsp
│   │       ├── WEB-INF/
│   │       └── META-INF/
│
├── docs/
│   ├── Deployment_Guide.pdf
│   ├── SRS_Document.pdf
│   ├── Project_Presentation.pdf
│   ├── Architecture_Diagram.png
│   ├── DFD_Level_0.png
│   ├── DFD_Level_1.png
│   └── Screenshots/
│
└── README.md
```

---

## 📘 Documentation

Complete project documentation is available in the `docs/` directory.

| Document                 | Description                                       |
| ------------------------ | ------------------------------------------------- |
| Deployment_Guide.pdf     | Complete Azure deployment and configuration guide |
| SRS_Document.pdf         | Software Requirements Specification               |
| Project_Presentation.pdf | Project presentation slides                       |
| Architecture_Diagram.png | System architecture                               |
| DFD_Level_0.png          | Context-level Data Flow Diagram                   |
| DFD_Level_1.png          | Detailed Data Flow Diagram                        |

---

## ⚙️ Quick Start

### Prerequisites

* Java JDK 17+
* Apache Tomcat
* Python 3.10+
* FFmpeg
* PostgreSQL
* Azure Subscription

### Install Python Dependencies

```bash
pip install -r requirements.txt
```

### Start FastAPI Server

```bash
uvicorn whisper_server:app --host 0.0.0.0 --port 8000
```

### Deploy Java Application

Deploy the Dynamic Web Project using:

* Eclipse IDE
* Apache Tomcat

Access:

```text
http://localhost:8080/ASRWebApp
```

### Detailed Deployment Instructions

For:

* Azure VM Setup
* PostgreSQL Configuration
* Azure Blob Storage Setup
* Azure Container Setup
* Azure Translator Configuration
* Database Schema Creation
* VS Code Integration
* Cost Analysis

Refer to:

```text
docs/Deployment_Guide.pdf
```

---

## 🔮 Future Enhancements

* Real-time subtitle generation
* Live meeting transcription
* Speaker diarization
* Multi-language subtitle export
* GPU accelerated inference
* Subtitle quality scoring
* Subtitle editing dashboard

---

## 📚 References

1. Faster-Whisper Documentation
   https://github.com/SYSTRAN/faster-whisper

2. FastAPI Documentation
   https://fastapi.tiangolo.com

3. Microsoft Azure Documentation
   https://learn.microsoft.com/azure

4. Azure AI Translator Documentation
   https://learn.microsoft.com/azure/ai-services/translator

5. FFmpeg Documentation
   https://ffmpeg.org

6. PostgreSQL Documentation
   https://www.postgresql.org/docs

---

## 👨‍💻 Author

**Vinit Shah**

Cloud Computing & Data Science Project

Developed using Java EE, Python, AI/ML, Multimedia Processing, and Microsoft Azure Cloud Services.
