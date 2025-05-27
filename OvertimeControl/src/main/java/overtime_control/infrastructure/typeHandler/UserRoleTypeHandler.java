package overtime_control.infrastructure.typeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import overtime_control.domain.model.UserRole;

public class UserRoleTypeHandler extends BaseTypeHandler<UserRole> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, UserRole parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setInt(i, parameter.getId());
	}

	@Override
	public UserRole getNullableResult(ResultSet rs, String columnName) throws SQLException {
		int value = rs.getInt(columnName);
		if (rs.wasNull()) {
			return null;
		}
		return UserRole.fromId(value);
	}

	@Override
	public UserRole getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		int value = rs.getInt(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		return UserRole.fromId(value);
	}

	@Override
	public UserRole getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		int value = cs.getInt(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		return UserRole.fromId(value);
	}
}

