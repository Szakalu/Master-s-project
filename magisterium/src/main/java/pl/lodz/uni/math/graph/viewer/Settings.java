package pl.lodz.uni.math.graph.viewer;

public class Settings {
	public static final String STYL_NORMALNEGO_WIERZCHOLKA = "shape: circle;"+"padding: 8px;"+"stroke-mode: plain;"+"size-mode: fit;"+"text-size: 10px;" + "fill-color: white;" + "text-color: black;";
	public static final String STYL_STARTOWEGO_WIERZCHOLKA = "shape: circle;"+"padding: 8px;"+"stroke-mode: plain;"+"size-mode: fit;"+"size: 20px;" + "text-size: 25px;" + "fill-color: yellow;" + "text-color: black;";
	public static final String STYL_KONCOWEGO_WIERZCHOLKA = "shape: circle;"+"padding: 8px;"+"stroke-mode: plain;"+"size-mode: fit;"+"size: 20px;" + "text-size: 25px;" + "fill-color: green;" + "text-color: black;";
	public static final String STYL_NORMALNEJ_KRAWEDZI = "size: 1px;"+"text-color: red;"+"fill-color: black;"+"text-size: 10px;"+"text-offset: -20;";
	public static final String STYL_ODWIEDZONEGO_WIERZCHOLKA = "size: 20px;" + "text-size: 10px;" + "fill-color: black;" + "text-color: red;";
	public static final String STYL_ODWIEDZONEGO_WIERZCHOLKA_Z_SASIADEM = "size: 20px;" + "text-size: 25px;" + "fill-color: black;" + "text-color: white;";
	public static final String STYL_AKTUALNEGO_WIERZCHOLKA = "size: 20px;" + "text-size: 25px;" + "fill-color: red;" + "text-color: black;";
	public static final String STYL_DODAWANEJ_KRAWEDZI_ODWIEDZANEJ_PLUS = "size: 2px;"+"text-color: red;"+ "fill-color: red;" +"text-size: 10px;"+"text-offset: -20;";
	public static final String UI_STYLE = "ui.style";
	public static final String UI_LABEL ="ui.label";
	public static final String UI_WEIGHT="weight";
	public static final Double SLEEP_TIME = 1d;
	public static final Double INITIAL_PHEROMONES = 1d;
	public static final Double ALFA = 1d;
	public static final Double BETA = 1d;
	//TODO Dodac styl krawedzi podzielony na party (part1 + size: ?px +part2) w zależności od ilosci feromonow jak jest 10x wieksza itd. do mniej wiecej 20px
}
