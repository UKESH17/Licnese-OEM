package com.htc.licenseapproval.entity.idgenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CustomHeaderGenerator implements IdentifierGenerator{

	private static final long serialVersionUID =  1L;

	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) {
		String PREFIX = "req-head-";
		String query ="SELECT COALESCE(MAX(CAST(SUBSTRING(request_header_id,10) AS UNSIGNED)),0)+1 FROM request_header ";
		try {
			Connection connection = session.getJdbcConnectionAccess().obtainConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet.next()) {
				return PREFIX +resultSet.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		throw new RuntimeException("Something went wrong !!");
	}

}
