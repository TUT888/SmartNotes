# SmartNotes - Note Taking Application

Personal project for smart note taking application

## Supported Features
The sample console app currently support below features
| Feature    | Description |
| -------- | ------- |
| View my note   | Choose a note to view its content  |
| Generate quiz from note | Choose a note to automatically generate relevant quizzes, this feature **requires HuggingFace API TOKEN** to run |
| Try sample quiz from sample response    | Try a sample quiz from sample response, this feature does not require API TOKEN  |

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
```
