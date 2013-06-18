import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class AllowIntranetFilter implements Filter
{
	public final static String[] IP_RANGES = {"192.168.", "127.0.", "10.8."};

	public AllowIntranetFilter()
	{
	}

	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{

		String ip = request.getRemoteAddr();

		HttpServletResponse httpResp = (HttpServletResponse) response;

		if (isAllowed(ip))
		{
			chain.doFilter(request, response);
		}
		else
		{
			httpResp.sendError(HttpServletResponse.SC_FORBIDDEN);
		}

	}

	private boolean isAllowed(String ip)
	{
		for (String ipRange : IP_RANGES)
		{
			if (ip.startsWith(ipRange)) {
				return true;
			}
		}
		return false;
	}

	public void destroy()
	{
	}
}