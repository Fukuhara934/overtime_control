package overtime_control.infrastructure.typeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import overtime_control.domain.model.WorkPattern;

public class WorkPatternTypeHandler extends BaseTypeHandler<WorkPattern> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, WorkPattern parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    @Override
    public WorkPattern getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int id = rs.getInt(columnName);
        return WorkPattern.fromId(id);
    }

    @Override
    public WorkPattern getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int id = rs.getInt(columnIndex);
        return WorkPattern.fromId(id);
    }

    @Override
    public WorkPattern getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
        int id = cs.getInt(columnIndex);
        return WorkPattern.fromId(id);
    }
}
