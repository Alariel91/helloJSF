package es.open4job.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import es.open4job.model.vo.*;
@ManagedBean
@SessionScoped
public class AparcamientoDAO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DataSource ds;

	public AparcamientoDAO() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/TestDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<AparcamientoVO> getallAparcamientos() {
		ArrayList<AparcamientoVO> listaAparcamientos = new ArrayList<AparcamientoVO>();

		try {
			Connection conexion = ds.getConnection();
			Statement stm = conexion.createStatement();
			ResultSet rs = stm
					.executeQuery("select * from APARCAMIENTOSPERDISCAP");
			while (rs.next()) {
				AparcamientoVO aparcamiento = new AparcamientoVO();
				aparcamiento.setId(rs.getInt("id"));
				aparcamiento.setLatitud(rs.getDouble("latitud"));
				aparcamiento.setLongitud(rs.getDouble("longitud"));
				aparcamiento.setTitulo(rs.getString("titulo"));
				aparcamiento.setIcono(rs.getString("icono"));
				listaAparcamientos.add(aparcamiento);
			}

		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(
					Level.SEVERE,
					"Error en AparcamientPD_DAO.getallAparcamientos:"
							+ e.getMessage());
		}
		return listaAparcamientos;
	}

	public AparcamientoVO getAparcamientoPDbyId(int id) {

		try {
			AparcamientoVO aparcamiento = new AparcamientoVO();
			Connection conexion = ds.getConnection();
			PreparedStatement pstm = conexion
					.prepareStatement("select * from APARCAMIENTOSPERDISCAP where id = ?");
			pstm.setFloat(1, id);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				aparcamiento.setId(rs.getInt("id"));
				aparcamiento.setLatitud(rs.getDouble("latitud"));
				aparcamiento.setLongitud(rs.getDouble("longitud"));
				aparcamiento.setTitulo(rs.getString("titulo"));
				aparcamiento.setIcono(rs.getString("icono"));
			}

			return aparcamiento;

		} catch (Exception e) {
			// TODO: handle exception
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					e.getMessage());
		}
		return null;
	}

	public void InsertarAparcamientoPDVO() {

		try {
			AparcamientoVO aparcamiento = new AparcamientoVO(224, 2153216,
					1685651, "Aparcamiento Discapacitados", "icono.png");
			Connection conexion = ds.getConnection();
			PreparedStatement pstm = conexion
					.prepareStatement("Insert into APARCAMIENTOSPERDISCAP Values (?,?,?,?,?)");

			pstm.setFloat(1, aparcamiento.getId());
			pstm.setDouble(2, aparcamiento.getLatitud());
			pstm.setDouble(3, aparcamiento.getLongitud());
			pstm.setString(4, aparcamiento.getTitulo());
			pstm.setString(5, aparcamiento.getIcono());

			pstm.execute();

		} catch (Exception e) {
			// TODO: handle exception
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					e.getMessage());
		}
	}

	public void ActualizarAparcamientoPDVO() {

		try {
			Connection conexion = ds.getConnection();
			PreparedStatement pstm = conexion
					.prepareStatement("Update APARCAMIENTOSPERDISCAP set titulo = ? Where id= ?");

			pstm.setString(1, "Aparcamiento");
			pstm.setInt(2, 1);

			pstm.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					e.getMessage());
		}
	}

	public void aparcamientosCSV() {
		File fichero;
		FileWriter fw;
		BufferedWriter bw;

		try {
			fichero = new File("/var/www/html/aparcamientos.csv");
			fw = new FileWriter(fichero);
			bw = new BufferedWriter(fw);

			ArrayList<AparcamientoVO> aparcamientos = new ArrayList<AparcamientoVO>();
			aparcamientos = getallAparcamientos();

			Iterator iterador = aparcamientos.iterator();

			while (iterador.hasNext()) {
				AparcamientoVO aparcamiento = (AparcamientoVO) iterador.next();
				bw.write(aparcamiento.getId() + ";" + aparcamiento.getTitulo()
						+ ";" + aparcamiento.getLatitud() + ";"
						+ aparcamiento.getLongitud() + ";"
						+ aparcamiento.getIcono());
				bw.write("\n");
			}
			bw.close();
			fw.close();
			System.out.println("El fichero ha sido creado correctamente");
		} catch (Exception e) {
			// TODO: handle exception
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Falla aparcamientosCSV:" + e.getMessage());
		}
	}

	public void PruebaInsertarCommit() {

		try {
			int id = 1;
			while (id % 2 != 0) {
				Random idRandom = new Random();
				id = idRandom.nextInt(10);
				System.out.println("ID 1" + id);
				AparcamientoVO aparcamiento = new AparcamientoVO(id, 2153216,
						1685651, "Aparcamiento de prueba", "icono.png");
				Connection conexion = ds.getConnection();
				PreparedStatement pstm = conexion
						.prepareStatement("Insert into APARCAMIENTOSPERDISCAP Values (?,?,?,?,?)");
				pstm.setFloat(1, aparcamiento.getId());
				pstm.setDouble(2, aparcamiento.getLatitud());
				pstm.setDouble(3, aparcamiento.getLongitud());
				pstm.setString(4, aparcamiento.getTitulo());
				pstm.setString(5, aparcamiento.getIcono());
				pstm.execute();
				id = idRandom.nextInt(10);
				System.out.println("ID 2" + id);
				if (id % 2 == 0) {
					conexion.commit();
					System.out.println("Se hace el commit");
				} else {
					conexion.rollback();
					System.out.println("No se hace el commit");
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					e.getMessage());
		}
	}
}
