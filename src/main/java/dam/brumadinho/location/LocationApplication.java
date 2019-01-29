package dam.brumadinho.location;

import com.opencsv.CSVWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocationApplication {

	private static final String MISSING_PEOPLE_URL = "http://brumadinho.vale.com/listagem-pessoas-sem-contato.html";

	private static final String CSV_PATH = "out/missing-people.csv";

	private static final String LAST_UPDATE_CRITERIA = "Última atualização em";

	public static void main(String[] args) throws IOException {
		Path path = getPath();
		try(CSVWriter writer = new CSVWriter(new FileWriter(path.toString()))) {
			final Document doc = Jsoup.connect(MISSING_PEOPLE_URL).get();
			writer.writeNext(new String[]{ doc.getElementsMatchingOwnText(LAST_UPDATE_CRITERIA).text() });

			writer.writeNext(new String[]{});

			doc
					.select("li")
					.stream()
					.map(Element::text)
					.forEach(o -> writer.writeNext(new String[]{o}));

			System.out.println("File created at " + path.toAbsolutePath());
		}
	}

	private static Path getPath() {
		return Paths.get(CSV_PATH);
	}

}