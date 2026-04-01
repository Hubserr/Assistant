package pl.project.Assistant;


public class TaskMapper {

        public static Task toEntity(TaskRequest request){
            Task task = new Task();
            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            task.setUntil(request.getUntil());
            task.setCompleted(request.isCompleted());
            return task;

        }

        public static TaskResponse toResponse(Task task){
            TaskResponse response = new TaskResponse();

            response.setId(task.getId());

            response.setTitle(task.getTitle());
            response.setCompleted(task.isCompleted());
            response.setDescription(task.getDescription());
            response.setUntil(task.getUntil());

            response.setCreatedAt(task.getCreatedAt());
            return response;

        }






}
