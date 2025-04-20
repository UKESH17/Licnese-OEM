package com.htc.licenseapproval.entity.idgenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.Generator;
import org.hibernate.id.IdentifierGenerator;

import com.htc.licenseapproval.entity.RequestDetails;
import com.htc.licenseapproval.enums.LicenseType;

public class CustomIDGenerator implements Generator, IdentifierGenerator {

	private static final long serialVersionUID = 1L;

	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) {
		RequestDetails requestDetails = (RequestDetails) object;
		String PREFIX = requestDetails.getLicenseDetails().getLicenseType().equals(LicenseType.PLURALS) ? "OEM-PLU-" : "OEM-LIN-";
		String query = "SELECT COALESCE(MAX(CAST(SUBSTRING(request_id, 9) AS UNSIGNED)),0) + 1 FROM request_details "
				+ "WHERE request_id LIKE ? ";
		try {
			Connection connection = session.getJdbcConnectionAccess().obtainConnection();
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, PREFIX + "%");

			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return PREFIX + resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Something went wrong !!");
	}

}
