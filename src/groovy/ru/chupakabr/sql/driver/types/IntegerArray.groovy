package ru.chupakabr.sql.driver.types

import org.postgresql.core.Oid
import org.postgresql.jdbc4.Jdbc4Array
import ru.chupakabr.hibernate.type.PostgreSQLTypes

import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Support for PostgreSQL integer[] type
 *
 * Created by myltik on 07/03/2014.
 */
class IntegerArray extends AbstractPostgresType {

    /**
     * JDBC type definition
     */
    public static final SQL_TYPES = [PostgreSQLTypes.ARRAY_INTEGER] as int[]

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] sqlTypes() {
        return SQL_TYPES
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) {
        def result = rs.getArray(names[0])
        if (result) {
            Object res = ((java.sql.Array)result).getArray()
            return res
        }

        return null
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nullSafeSet(PreparedStatement stmt, Object value, int index) {
        def makeArray = {
            "{${value.join(',')}}"
        }

        java.sql.Array arrayValue = new Jdbc4Array(
                getConnection(stmt),
                (int) Oid.INT4_ARRAY,
                (String)(value == null ? null : makeArray()))
        stmt.setArray(index, arrayValue)
    }
}
