package overtime_control.domain.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import overtime_control.domain.model.MUser;
import overtime_control.domain.service.UserService;
import overtime_control.domain.userInform.UserInform;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		MUser loginUser = userService.getUserByEmail(email);

		if (loginUser == null) {
			throw new UsernameNotFoundException("user not found");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		

		authorities.add(new SimpleGrantedAuthority(loginUser.getRole().getSecurityRole()));

		return new UserInform(
				loginUser.getEmail(),
				loginUser.getPassword(),
				authorities,
				loginUser.getId(),
				loginUser.getFamilyName(),
				loginUser.getFirstName(),
				loginUser.getDepartment().getDepartmentName());

	}
}
