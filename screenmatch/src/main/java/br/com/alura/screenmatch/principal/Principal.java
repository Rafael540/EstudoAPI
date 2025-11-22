package br.com.alura.screenmatch.principal;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.screenmatch.model.DadosEpisodios;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporadas;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.services.ConsumoAPI;
import br.com.alura.screenmatch.services.ConverteDados;

public class Principal {
	private Scanner leitura = new Scanner(System.in);
	private ConsumoAPI consumo = new ConsumoAPI();

	private ConverteDados conversor = new ConverteDados();

	private final String ENDERECO = "https://www.omdbapi.com/?i=";
	private final String API_KEY = "&apikey=b166411f";

	public void exibeMenu() {
		System.out.println("Digite o nome da série para busca: ");
		var nomeSerie = leitura.nextLine();
		var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		List<DadosTemporadas> temporadas = new ArrayList<>();
		for (int i = 1; i <= dados.totalTemporadas(); i++) {
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporadas dadosTemporada = conversor.obterDados(json, DadosTemporadas.class);
			temporadas.add(dadosTemporada);
		}

		temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

//		List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");
//		nomes.stream()
//			.sorted()
//			.limit(3)
//			.filter(n -> n.startsWith("N"))
//			.map(n -> n.toUpperCase())
//			.forEach(System.out::println);

		List<DadosEpisodios> dadosEpisodios = temporadas.stream().flatMap(t -> t.episodios().stream())
				.collect(Collectors.toList());

//		System.out.println("\nTop 5 episódios");
//		dadosEpisodios.stream()
//				.filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//				.peek( e -> System.out.println("Primeiro filtro(N/A) " + e))
//				.sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed()).limit(5)
//				.peek( e -> System.out.println("Ordenação" + e))
//				.limit(5)
//				.peek( e -> System.out.println("Limite " + e))
//				.map(e -> e.titulo().toUpperCase())
//				.peek( e -> System.out.println("Mapeamento " + e))
//				.forEach(System.out::println);
	
		List<Episodio> episodios =  temporadas.stream()
				.flatMap(t -> t.episodios().stream()
				.map(d -> new Episodio(t.numero(), d))
				).collect(Collectors.toList());
		
		episodios.forEach(System.out::println);
		
		
		System.out.print("Digite um trecho do título do episódio: ");
		var trechoTitulo = leitura.nextLine();
		Optional<Episodio> episodioBuscado = episodios.stream()
				.filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
				.findFirst();
		if(episodioBuscado.isPresent()) {
			System.out.println("Episódio encontrado!");
			System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
		} else {
			System.out.println("Episódio não encontrado!");
		}
		
//		System.out.println("A partir de que ano você deseja ver os episódios? ");
//		var ano = leitura.nextInt();
//		leitura.nextLine();
//		
//		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//		
//		LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//		episodios.stream()
//				.filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//				.forEach(e -> System.out.println(
//						"Temporada: " + e.getTemporada()+
//						"Episodio: " + e.getTitulo()+
//						"Data lançamento: " + e.getDataLancamento().format(formatador)));
		
		Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
				.filter(e -> e.getAvaliacao() > 0.0)
				.collect(Collectors.groupingBy(Episodio::getTemporada, 
						Collectors.averagingDouble(Episodio::getAvaliacao)));
		System.out.println(avaliacoesPorTemporada); 
		
		DoubleSummaryStatistics est = episodios.stream()
				.filter(e -> e.getAvaliacao() > 0.0)
				.collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
		System.out.println(est);
	}	
}
