package overtime_control.infrastructure.typeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import overtime_control.domain.model.OvertimeStatus;

public class OvertimeStatusTypeHandler extends BaseTypeHandler<OvertimeStatus> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, OvertimeStatus parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setInt(i, parameter.getId());
	}

	@Override
	public OvertimeStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
		int value = rs.getInt(columnName);
		return OvertimeStatus.fromId(value);
	}

	@Override
	public OvertimeStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		int value = rs.getInt(columnIndex);
		return OvertimeStatus.fromId(value);
	}

	@Override
	public OvertimeStatus getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
		int value = cs.getInt(columnIndex);
		return OvertimeStatus.fromId(value);
	}
}
