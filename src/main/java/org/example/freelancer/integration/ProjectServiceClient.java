package org.example.freelancer.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.freelancer.dto.ProjectDTO;
import org.example.freelancer.dto.FreelancerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${project.service.url:http://localhost:8081}")
    private String projectServiceUrl;

    // Fetch newly added projects (for freelancers)
    public Flux<ProjectDTO> fetchRecentProjects() {
        return webClientBuilder.build()
                .get()
                .uri(projectServiceUrl + "/api/projects")
                .retrieve()
                .bodyToFlux(ProjectDTO.class)
                .doOnError(ex -> log.error("Error fetching projects: {}", ex.getMessage()));
    }

    // Fetch top freelancers (for clients)
    public Flux<FreelancerDTO> fetchTopFreelancers() {
        return webClientBuilder.build()
                .get()
                .uri(projectServiceUrl + "/api/freelancers/top")
                .retrieve()
                .bodyToFlux(FreelancerDTO.class)
                .doOnError(ex -> log.error("Error fetching top freelancers: {}", ex.getMessage()));
    }
}
