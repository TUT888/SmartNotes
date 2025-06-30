# SmartNotes - Note Taking Application

Personal project for smart note taking application

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