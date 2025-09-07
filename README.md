# SmartNotes Backend

Personal project for smart note taking application

## Supported Features
| Module | Method | API | Description |
| -------- | -------- | -------- | ------- |
| Document | GET | `/api/document/all` | List all available documents  |
| Note | GET | `/api/note/{id}` | Get note content using its id |
| | POST | `/api/note/create` | Create new note |
| AI | GET | `/api/ai/generateQuiz/sample` | Get a sample quiz from sample response, this API does not require API TOKEN |
| | GET | `/api/ai/generateQuiz/{noteId}` | Generate relevant quizzes based on 1 note, this feature **requires HuggingFace API TOKEN** to run |

## Project Setup
### HuggingFace API
- Create a HuggingFace account
- Create Access Token:
  - Click on your profile icon and select Access Token
  - Click Create New Token
  - Enable the Make calls to inference provider permission
- Accept Model Usage Conditions:
  - Navigate to the model's page, select a model you wish to use
  - Review and accept the conditions to use the model

### Environment variables
In project root directory, create `.env` file and paste your **ACCESS TOKEN** here
```
API_TOKEN=<YOUR-TOKEN-HERE>
API_URL=<YOUR-INFERENCE-PROVIDER>
MODEL=<YOUR-SELECTED-MODEL>

AI_API_USER_ROLE=user
AI_API_SYSTEM_ROLE=system
AI_API_TEMPERATURE=<YOUR-CUSTOM-VALUE>
AI_API_TOP_P=<YOUR-CUSTOM-VALUE>
  
# Database
DB_PORT=<YOUR-DATABASE-PORT>
DB_NAME=<YOUR-DATABASE-NAME>
DB_USERNAME=<YOUR-DATABASE-DB_USERNAME>
DB_PASSWORD=<YOUR-DATABASE-DB_PASSWORD>
```