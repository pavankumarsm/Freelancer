package org.example.freelancer.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.freelancer.integration.ProjectServiceClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DashboardScheduler {

    private final ProjectServiceClient projectClient;

    // Refresh every minute for freelancers (show new projects)
    @Scheduled(fixedRate = 60000)
    public void updateFreelancerDashboard() {
        projectClient.fetchRecentProjects()
                .collectList()
                .subscribe(projects -> {
                    log.info("Fetched {} new projects for freelancer dashboard.", projects.size());
                    // Here you can cache it in Redis or send via WebSocket to UI
                });
    }

    // Refresh every 2 minutes for clients (show top freelancers)
    @Scheduled(fixedRate = 120000)
    public void updateClientDashboard() {
        projectClient.fetchTopFreelancers()
                .collectList()
                .subscribe(freelancers -> {
                    log.info("Fetched {} top freelancers for client dashboard.", freelancers.size());
                });
    }
}
