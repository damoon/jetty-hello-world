import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.yammer.metrics.reporting.AdminServlet;
import com.yammer.metrics.reporting.HealthCheckServlet;
import com.yammer.metrics.reporting.MetricsServlet;
import com.yammer.metrics.reporting.PingServlet;
import com.yammer.metrics.reporting.ThreadDumpServlet;

public class AppLauncher
{
	public static void main(String[] args) throws Exception
	{
		final Injector injector = Guice.createInjector(new JerseyServletModule()
		{
			@Override
			protected void configureServlets()
			{
				// Must configure at least one JAX-RS resource or the
				// server will fail to start.
				bind(HelloGuice.class);
				bind(GuicyInterface.class).to(GuicyInterfaceImpl.class);

		        Map<String, String> params = new HashMap<>();
		        params.put("com.sun.jersey.config.property.packages","com.yammer.metrics.jersey");
		        serve("/*").with(GuiceContainer.class, params);
			}
		});

		final Server server = new Server(8080);

		final ServletContextHandler app = new ServletContextHandler(server, "/");
		
		app.addFilter(AllowIntranetFilter.class, "/monitoring", null);
		app.addFilter(AllowIntranetFilter.class, "/monitoring/*", null);

		app.addServlet(new ServletHolder(new AdminServlet()), "/monitoring");
		app.addServlet(new ServletHolder(new HealthCheckServlet()), "/monitoring/healthcheck");
		app.addServlet(new ServletHolder(new MetricsServlet()), "/monitoring/metrics");
		app.addServlet(new ServletHolder(new PingServlet()), "/monitoring/ping");
		app.addServlet(new ServletHolder(new ThreadDumpServlet()), "/monitoring/threads");
		app.addFilter(SkipOtherFilters.class, "/monitoring", null);
		app.addFilter(SkipOtherFilters.class, "/monitoring/healthcheck", null);
		app.addFilter(SkipOtherFilters.class, "/monitoring/metrics", null);
		app.addFilter(SkipOtherFilters.class, "/monitoring/ping", null);
		app.addFilter(SkipOtherFilters.class, "/monitoring/threads", null);
		
		app.addEventListener(new InjectorProvider(injector));
		app.addFilter(GuiceFilter.class, "/*", null);

		app.addServlet(DefaultServlet.class, "/");


		// Start the server
		server.start();
		server.join();
	}
}