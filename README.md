# SmartNotes - Note Taking Application

Personal project for smart note taking application

## About
- Input notes are saved at `/data`, which is in markdown format as example
- Output quizzes are saved at `/output`, including
    - Raw response from LLM as text
    - Processed quizzes from raw response as json

## How to run 
1. HuggingFace setup
    - Create a HuggingFace account and accept the conditions to use the model, select a model you wish to use
    - Create Access Token by clicking on your profile -> Access Token
2. Create `.env` file with your generated token
    ```
    API_TOKEN=<YOUR-TOKEN-HERE>
    API_URL=<YOUR-INFERENCE-PROVIDER>
    MODEL=<YOUR-SELECTED-MODEL>
    ```
3. Clone this repo branch (or clone repo and checkout branch later)
    ```bash
    git clone -b demo-ai-python https://github.com/TUT888/SmartNotes.git
    ```
4. Setup python env (either venv or conda) and activate your env:
    ```bash
    conda create --name smart-notes requests dotenv
    conda activate smart-notes
    ```
5. Run the python file
    ```python
    python test.py
    ```
