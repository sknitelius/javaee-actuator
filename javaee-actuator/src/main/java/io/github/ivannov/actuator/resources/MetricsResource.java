package io.github.ivannov.actuator.resources;

import io.github.ivannov.actuator.inspectors.JmxInspector;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("/metrics")
@RequestScoped
public class MetricsResource {

    @Inject
    private JmxInspector jmxInspector;

	@GET
	@Produces("application/json")
	public Response getResourceUsage() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("cpu", jmxInspector.getProcessCPU());
        Map<String, Long> heapMemory = jmxInspector.getHeapMemory();
        heapMemory.entrySet().forEach(entry -> jsonObjectBuilder.add("heap." + entry.getKey(), entry.getValue()));
        Map<String, Integer> threadInfo = jmxInspector.getThreadsInfo();
        threadInfo.entrySet().forEach(entry -> jsonObjectBuilder.add(entry.getKey(), entry.getValue()));
        Map<String, Long> loadedClassesInfo = jmxInspector.getLoadedClassesInfo();
        loadedClassesInfo.entrySet().forEach(entry -> jsonObjectBuilder.add(entry.getKey(), entry.getValue()));
        Map<String, Long> gcInfo = jmxInspector.getGCInfo();
        gcInfo.entrySet().forEach(entry -> jsonObjectBuilder.add("gc." + entry.getKey(), entry.getValue()));
        return Response.ok(jsonObjectBuilder.build()).build();
	}
}