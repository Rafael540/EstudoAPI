package br.com.alura.screenmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.screenmatch.model.DadosEpisodios;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.services.ConsumoAPI;
import br.com.alura.screenmatch.services.ConverteDados;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoapi = new ConsumoAPI();
		var json = consumoapi.obterDados("https://www.omdbapi.com/?i=smallville&Season=1&apikey=b166411f");
		System.out.println(json);
		json = consumoapi.obterDados("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
		json = consumoapi.obterDados("http://www.omdbapi.com/?i=smallville&season=1&episode=2&apikey=b166411f");
		DadosEpisodios dadosEpisodio = conversor.obterDados(json, DadosEpisodios.class);
		System.out.println(dadosEpisodio);
	}

}
