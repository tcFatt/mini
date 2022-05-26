package nusiss.mini;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import nusiss.mini.filter.AuthenticationFilter;


@SpringBootTest
class TestApplicationTests {

	private static final AuthenticationFilter filter = new AuthenticationFilter();

	@Test
	public void testDoFilterShouldNotSendRedirect() throws Exception {
		MockHttpSession session = new MockHttpSession();
    	MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
		session.setAttribute("username", "sun");
    	request.setSession(session);
		request.setRequestURI("/secure/home");
		HttpSession sess = request.getSession();
		String username = (String)sess.getAttribute("username");
		filter.doFilter(request, response, chain);
		assertEquals("sun", username);
		assertEquals("/secure/home", request.getRequestURI().toString());
		assertNotEquals("/", response.getRedirectedUrl());
	}

	@Test
	public void testDoFilterShouldSendRedirect() throws Exception {
		MockHttpSession session = new MockHttpSession();
    	MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
		session.setAttribute("username", null); // username is null
		request.setSession(session);
		filter.doFilter(request, response, chain);
		assertEquals("/", response.getRedirectedUrl());
	}

	@Test
	public void main() {
	   MiniApplication.main(new String[] {});
	}

}