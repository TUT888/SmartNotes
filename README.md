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
- Create a HuggingFace account and accept the conditions to use the model: [HuggingFace - Google/Gemma-2-2b-it](https://huggingface.co/google/gemma-2-2b-it)
- Create Access Token by clicking on your profile -> Access Token -> Follow the instruction and check all permission

### Environment variables
In project root directory, create `.env` file with your generated token
```
API_TOKEN=<YOUR-TOKEN-HERE>
API_URL=https://router.huggingface.co/nebius/v1/chat/completions
MODEL=google/gemma-2-2b-it
```