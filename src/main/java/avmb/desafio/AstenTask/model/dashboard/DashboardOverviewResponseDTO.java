package avmb.desafio.AstenTask.model.dashboard;


public record DashboardOverviewResponseDTO(long totalProjects, long totalTasksAssigned, long tasksCompleted, long tasksPending) {
}
