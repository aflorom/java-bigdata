package aflorom;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

import com.google.protobuf.ServiceException;
import com.opencsv.CSVReader;

public class Fase1 {

	static Admin admin = null;
	static Connection connection = null;
	static ArrayList<ArrayList<String>> data = null;

	public static void main(String[] args) throws IOException, ServiceException {

		Integer rows = 2;
		Integer columns = 3;

		Configuration configuration = HBaseConfiguration.create();

		System.out.println("Processing!");

		configuration.set("hbase.master.info.port", "16010");
		configuration.set("hbase.zookeeper.property.clientPort", "2181");
		configuration.set("hbase.cluster.distributed", "true");
		configuration.set("hbase.master.port", "16000");
		configuration.set("hbase.zookeeper.quorum", "zoo");
		configuration.set("hbase.master", "hbase-master:16000");
		configuration.set("hbase.regionserver.port", "16020");
		configuration.set("hbase.regionserver.info.port", "16030");
		configuration.set("hbase.master.hostname", "hbase-master");
		configuration.set("hbase.rootdir", "hdfs://namenode:9000/hbase");

		connection = ConnectionFactory.createConnection(configuration);

		admin = connection.getAdmin();

		String tableToCreate = "capTable9";

		List<String[]> data = loadFromFile("SET-dec-2013.csv");

		// dataHerramientaDeCarga = herramientaDeCarga(tableToCreate,
		// "SET-dec-2013.csv", rows, columns);

		// herramientaDeCarga(tableToCreate, "SET-dec-2013.csv", rows, columns);

		// viewData(data, tableToCreate, rows, columns);

		herramientaDeExtraccion(tableToCreate, "SET-dec-2013.csv", rows, columns);

		System.out.println("Finished!");

		// extraccionDeInformacion("SET-dec-2013.csv", rows, columns);
		// formatoDeSalida("SET-dec-2013.csv", rows, columns);
	}

	public static void viewData(List<String[]> data, String table, Integer rows, Integer columns) {

		final TableName tableName = TableName.valueOf(table);

		try {

			Table tableTable = connection.getTable(tableName);

			for (int i = 0; i < 100; i++) {

				for (int j = 1; j <= rows; j++) {

					Get g = new Get(Bytes.toBytes(j + data.get(i)[0] + ":" + data.get(i)[1])); // Instantiate Get class

					Result result = tableTable.get(g); // Read the data

					byte[] sensorRow = result.getValue(Bytes.toBytes("sensor"), Bytes.toBytes("sensor"));
					byte[] datetimeRow = result.getValue(Bytes.toBytes("datetime"), Bytes.toBytes("datetime"));

					String sensorValue = Bytes.toString(sensorRow); // Print the values
					String datetimeValue = Bytes.toString(datetimeRow); // Print the values

					System.out.print(sensorValue + " | " + datetimeValue);

					for (int k = 0; k < columns; k++) {

						byte[] measureRow = result.getValue(Bytes.toBytes("measure"),
								Bytes.toBytes("measure" + (k + 1)));

						String measureValue = Bytes.toString(measureRow); // Print the values

						System.out.print(" | ");
						System.out.print(measureValue);

					}

					System.out.println("");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String[]> loadFromFile(String file) throws FileNotFoundException, IOException {

		List<String[]> r = null;

		try (CSVReader reader = new CSVReader(new FileReader(file))) {
			r = reader.readAll();
			// r.forEach(x -> System.out.println(Arrays.toString(x)));
		}

		return r;
	}

	public static ArrayList<ArrayList<String>> herramientaDeCarga(String table, String file, Integer rows,
			Integer columns) throws FileNotFoundException, IOException {

		// TableDescriptorBuilder tableDescriptorBuilder =
		// TableDescriptorBuilder.newBuilder(TableName.valueOf(table));

		// final TableName tableName = TableName.valueOf(table);

		// Table tableTable = connection.getTable(tableName);

		// ColumnFamilyDescriptor sensor =
		// ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("sensor")).build();
		// ColumnFamilyDescriptor datetime =
		// ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("datetime")).build();
		// ColumnFamilyDescriptor measure =
		// ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("measure")).build();

		// tableDescriptorBuilder.setColumnFamily(sensor);
		// tableDescriptorBuilder.setColumnFamily(datetime);
		// tableDescriptorBuilder.setColumnFamily(measure);

		// admin.createTable(tableDescriptorBuilder.build());

		List<String[]> data = loadFromFile(file);

		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < 10; i++) {

			for (int j = 1; j <= rows; j++) {

				ArrayList<String> rowData = new ArrayList<String>();

				rowData.add(j + data.get(i)[0]);
				rowData.add(data.get(i)[1]);

				// Put p = new Put(Bytes.toBytes(j + data.get(i)[0] + ":" + data.get(i)[1]));

				// p.addColumn(Bytes.toBytes("sensor"), Bytes.toBytes("sensor"), Bytes.toBytes(j
				// + data.get(i)[0]));
				// p.addColumn(Bytes.toBytes("datetime"), Bytes.toBytes("datetime"),
				// Bytes.toBytes(data.get(i)[1]));

				for (int k = 0; k < columns; k++) {

					rowData.add(data.get(i)[2]);

					// p.addColumn(Bytes.toBytes("measure"), Bytes.toBytes("measure" + (k + 1)),
					// Bytes.toBytes(data.get(i)[2]));

				}

				// tableTable.put(p);

				list.add(rowData);

			}
		}

		// list.forEach(x -> System.out.println(x.toString()));

		return list;

	}

	public static ArrayList<ArrayList<String>> herramientaDeExtraccion(String table, String file, Integer rows,
			Integer columns) throws FileNotFoundException, IOException {

		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> data = loadRowsColumns(file, rows, columns);

		Integer start = 0;

		for (int i = start; i < data.size(); i++) {

			if (data.get(i).get(0).charAt(0) == rows.toString().charAt(0)) {

				ArrayList<String> rowData = new ArrayList<String>();

				rowData.add(data.get(i).get(0));
				rowData.add(data.get(i).get(1).substring(0, 10));

				for (int j = i; j < i + 144; j++) {

					if (data.get(j).get(0).charAt(0) == rows.toString().charAt(0)) {

						rowData.add(data.get(j).get(2));

					}
				}

				start = start + 144;

				list.add(rowData);

			}
		}

		list.forEach(x -> System.out.println(x.toString()));

		return list;

	}

	public static ArrayList<ArrayList<String>> loadRowsColumns(String file, Integer rows, Integer columns)
			throws FileNotFoundException, IOException {

		List<String[]> data = loadFromFile(file);

		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < data.size(); i++) {

			for (int j = 1; j <= rows; j++) {

				ArrayList<String> rowData = new ArrayList<String>();

				if (j == rows) {
					
					rowData.add(j + data.get(i)[0]);
					rowData.add(data.get(i)[1]);
					rowData.add(data.get(i)[2]);

					list.add(rowData);
				}
			}
		}

		list.forEach(x -> System.out.println(x.toString()));

		return list;
	}
}
