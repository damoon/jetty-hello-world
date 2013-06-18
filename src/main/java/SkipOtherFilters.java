import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class SkipOtherFilters implements Filter
{
	FilterConfig config;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{    
		config = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{
		request.getRequestDispatcher(((HttpServletRequest) request).getServletPath()).forward(request, response);
	}

	@Override
	public void destroy()
	{
	}

}
