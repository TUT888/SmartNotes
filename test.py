import re
import os
import requests
import json
from dotenv import load_dotenv
from string import Template

load_dotenv()

# Load ENV
API_URL = os.getenv("API_URL", "")
API_TOKEN = os.getenv("API_TOKEN", "")
MODEL = os.getenv("MODEL", "")

# AI API Header
HEADERS = {"Authorization": f"Bearer {API_TOKEN}"}
PROMPT_FILE_PATH = "prompt.txt"

def create_prompt_for_note(format_type, language_used, note_file_path):
  prompt = ""
  try:
    # Read note content
    with open(note_file_path, 'r', encoding='utf-8') as file:
      note_content = file.read()

    # Read prompt format, and replace the variables with input content
    with open(PROMPT_FILE_PATH, "r", encoding="utf-8") as f:
      src = Template(f.read())
      prompt = src.substitute({
        "format_type": format_type,
        "language_used": language_used,
        "student_note": note_content
      })
  except Exception as e:
    print(f"An error occurred: {e}")
  return prompt

def fetchQuizFromLlama(prompt):
  response = requests.post(
    API_URL, 
    headers = HEADERS, 
    json={
      "messages": [
        {
          "role": "user",
          "content": prompt
        }
      ],
      "model": MODEL,
      "max_tokens": 1000,
      "temperature": 0.7,
      "top_p": 0.9
    })
  if response.status_code == 200:
    result = response.json()["choices"][0]["message"]["content"]
    return result
  else:
    raise Exception(f"API request failed: {response.status_code} - {response.text}")

def process_quiz(quiz_text):
  questions = []
  pattern = re.compile(
    r'\*\*QUESTION \d+:\*\*\s+(.+?)\s+'
    r'\*\*OPTION A:\*\*\s+(.+?)\s+'
    r'\*\*OPTION B:\*\*\s+(.+?)\s+'
    r'\*\*OPTION C:\*\*\s+(.+?)\s+'
    r'\*\*OPTION D:\*\*\s+(.+?)\s+'
    r'\*\*ANS:\*\*\s+([A-D])',
    re.DOTALL
  )
  matches = pattern.findall(quiz_text)

  for match in matches:
    question = match[0].strip()
    options = [match[1].strip(), match[2].strip(), match[3].strip(), match[4].strip()]
    correct_ans = match[5].strip()

    question_data = {
      "question": question,
      "options": options,
      "correct_answer": correct_ans
    }
    questions.append(question_data)

  return questions

if __name__ == '__main__':
  for filename in os.listdir("data"):
    file_path = f"data/{filename}" 

    prompt = create_prompt_for_note(format_type="markdown", language_used="english", note_file_path=file_path)
    raw_quiz = fetchQuizFromLlama(prompt)
    processed_quiz = process_quiz(raw_quiz)
    with open(f"output/raw-{filename.split(".")[0]}.txt", "w", encoding="utf-8") as f:
      f.write(raw_quiz)
    with open(f"output/processed-{filename.split(".")[0]}.json", "w", encoding="utf-8") as f:
      json.dump(processed_quiz, f, ensure_ascii=False)