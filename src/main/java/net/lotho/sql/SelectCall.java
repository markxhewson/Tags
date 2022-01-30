package net.lotho.sql;

import java.sql.ResultSet;

public interface SelectCall {
    void call(ResultSet resultSet);
}
