package overtime_control.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import overtime_control.domain.model.OvertimeStatus;
import overtime_control.domain.model.UserRole;
import overtime_control.domain.model.WorkPattern;

@Configuration
public class JavaConfig {

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	
	@Bean
	Converter<String, UserRole> userRoleConverter() {
		return new Converter<String, UserRole>() {
			@Override
			public UserRole convert(String id) {
				if (id == null || id.isEmpty()) {
					return null;
				}
				try {
					// 文字列をIDとして変換し、UserRoleを取得
					return UserRole.fromId(Integer.parseInt(id));
				} catch (NumberFormatException e) {
					// 数字に変換できない場合はnullを返す
					return null;
				}
			}
		};
	}

	
	@Bean
	Converter<String, WorkPattern> workPatternConverter() {
	    return new Converter<String, WorkPattern>() {
	        @Override
	        public WorkPattern convert(String name) {
	            if(name == null || name.isEmpty()) {
	                return null;
	            }
	            try {
	                return WorkPattern.valueOf(name);
	            } catch(IllegalArgumentException e) {
	                return null;
	            }
	        }
	    };
	}

	
	@Bean
	Converter<String, OvertimeStatus> OvertimeStatusConverter() {
		return new Converter<String, OvertimeStatus>() {
			@Override
			public OvertimeStatus convert(String id) {
				if (id == null || id.isEmpty()) {
					return null;
				}
				try {
					return OvertimeStatus.fromId(Integer.parseInt(id));
				} catch (NumberFormatException e) {
					return null;
				}
			}
		};
	}
	

}
