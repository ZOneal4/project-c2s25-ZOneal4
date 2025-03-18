import java.util.*;

// Player class
class Player {
    private String name;
    private int battingSkill;
    private int pitchingSkill;

    public Player(String name, int battingSkill, int pitchingSkill) {
        this.name = name;
        this.battingSkill = battingSkill;
        this.pitchingSkill = pitchingSkill;
    }

    public String getName() { return name; }
    public int getBattingSkill() { return battingSkill; }
    public int getPitchingSkill() { return pitchingSkill; }

    public void improveSkills() {
        this.battingSkill += (int)(Math.random() * 5);
        this.pitchingSkill += (int)(Math.random() * 5);
    }
}

// Team class
class Team {
    private String name;
    private List<Player> roster;
    private int wins;
    private int losses;

    public Team(String name) {
        this.name = name;
        this.roster = new ArrayList<>();
        this.wins = 0;
        this.losses = 0;
    }

    public void addPlayer(Player player) { roster.add(player); }
    public void removePlayer(Player player) { roster.remove(player); }
    public String getName() { return name; }
    public List<Player> getRoster() { return roster; }
    public void addWin() { wins++; }
    public void addLoss() { losses++; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }

    public void developPlayers() {
        for (Player player : roster) {
            player.improveSkills();
        }
    }
}

// Match class
class Match {
    private Team homeTeam;
    private Team awayTeam;

    public Match(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public Team playBestOfThree() {
        int homeWins = 0;
        int awayWins = 0;
        while (homeWins < 2 && awayWins < 2) {
            int homeScore = simulateScore(homeTeam);
            int awayScore = simulateScore(awayTeam);
            if (homeScore > awayScore) {
                homeWins++;
            } else {
                awayWins++;
            }
        }
        return homeWins == 2 ? homeTeam : awayTeam;
    }

    private int simulateScore(Team team) {
        Random rand = new Random();
        int totalSkill = 0;
        for (Player p : team.getRoster()) {
            totalSkill += p.getBattingSkill();
        }
        return rand.nextInt(Math.max(1, totalSkill / 10) + 1); // Score based on team skill
    }
}

// GameManager class
class GameManager {
    private List<Team> teams;
    private Team userTeam;

    public GameManager() {
        teams = new ArrayList<>();
    }

    public void createTeams() {
        String[] teamNames = {"Red Sox", "Yankees", "Dodgers", "Giants", "Cubs", "White Sox", "Astros", "Mets", "Braves", "Phillies",
                "Cardinals", "Pirates", "Reds", "Brewers", "Rockies", "Padres", "Diamondbacks", "Marlins", "Blue Jays", "Rangers",
                "Orioles", "Tigers", "Royals", "Twins", "Athletics", "Mariners", "Rays", "Indians", "Nationals", "Angels"};

        for (String name : teamNames) {
            Team team = new Team(name);
            for (int i = 0; i < 5; i++) { // Add 5 players per team
                team.addPlayer(new Player("Player " + i, (int)(Math.random() * 100), (int)(Math.random() * 100)));
            }
            teams.add(team);
        }
    }

    public void startScreen() {
        System.out.println("====================================");
        System.out.println(" Welcome To Baseball Manager Tycoon ");
        System.out.println("====================================");
        System.out.println("Select your team to begin!\n");
    }

    public void pickTeam() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Pick a team:");
        for (int i = 0; i < teams.size(); i++) {
            System.out.println((i + 1) + ". " + teams.get(i).getName());
        }
        int choice = scanner.nextInt() - 1;
        userTeam = teams.get(choice);
        System.out.println("You chose: " + userTeam.getName());
        postSelectionMenu();
    }

    public void postSelectionMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nNow that you've selected your team, what would you like to do?");
        while (true) {
            System.out.println("1. View Roster");
            System.out.println("2. Start Season");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewRoster();
                    break;
                case 2:
                    simulateSeason();
                    return;
                case 3:
                    System.out.println("Exiting game...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public void viewRoster() {
        System.out.println("\nRoster for " + userTeam.getName() + ":");
        for (Player player : userTeam.getRoster()) {
            System.out.println(player.getName() + " - Batting Skill: " + player.getBattingSkill() + ", Pitching Skill: " + player.getPitchingSkill());
        }
    }

    public void simulateSeason() {
        System.out.println("Starting the 60-game season for " + userTeam.getName() + "...");
        for (int i = 0; i < 60; i++) {
            Collections.shuffle(teams);
            for (int j = 0; j < teams.size(); j += 2) {
                if (j + 1 < teams.size()) {
                    Match match = new Match(teams.get(j), teams.get(j + 1));
                    match.playBestOfThree();
                }
            }
        }
        for (Team team : teams) {
            team.developPlayers(); // Player development after season
        }
        printStandings();
    }

    public void printStandings() {
        System.out.println("Final Standings:");
        teams.sort((a, b) -> b.getWins() - a.getWins());
        for (Team team : teams) {
            System.out.println(team.getName() + " - " + team.getWins() + " Wins, " + team.getLosses() + " Losses");
        }
    }
}

// Main class
public class BaseballManager {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.createTeams();
        gameManager.startScreen();
        gameManager.pickTeam();
    }
}


