package model;


import data.GameDA;
import gui.GameGui;
import gui.GameOverFrame;
import java.awt.Frame;
import java.util.List;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import model.companions.AutoShooter;
import model.companions.Companion;
import model.companions.LifeSaver;
import model.companions.Miner;
import model.companions.Shooter;
import model.power.Power;

import multiplayer.Server;

public class Game implements Serializable, Runnable {

    private Random randomGenerator = new Random();
    private int fps = 60;
    private int gameHeight;
    private int gameWidth;
    private int spawnEnemyX;
    private int spawnEnemyY;
    private int waveCounter;
    private long spawnTimer;
    private Character character;
    private Character character2;
    private InputHandler handler;
    private List<Bullet> bullets = new CopyOnWriteArrayList<>();
    private List<Enemy> enemies = new CopyOnWriteArrayList<>();
    private List<Geom> geoms = new CopyOnWriteArrayList<>();
    private List<Wave> waves = new ArrayList<>();
    private List<Mine> mines = new ArrayList<>();
    private List<Power> powers = new CopyOnWriteArrayList<>();

    private GameGui gameGui;
    private List<Player> players;
    private int companionid;
    private Server server;
    private Companion companion;
    private GameDA db = GameDA.getInstance();
    
    private int characterid;
    private long delaytime;
    private boolean multiplayer;
    private CollisionDetection collisionDetection = new CollisionDetection();
    private String moeilijkheidsgraad;
    private int playerid; 

    // meegeven in constructor : 
    public Game(boolean multiplayer,String moeilijkheidsgraad,int playerid,int characterid, int companionid) {
        this.multiplayer = multiplayer;
        this.moeilijkheidsgraad = moeilijkheidsgraad;
        this.companionid = companionid;
        this.characterid = characterid;
        this.playerid = playerid;
        
    }

    public void run() {
        init();
        makeWaves();
        for(Wave v: waves){
            v.setMoeilijkheidsgraad(moeilijkheidsgraad);
        }

        long spawnTimer = System.currentTimeMillis();
        waveCounter = 0;
        boolean keepGoing = true;
        while (keepGoing) {
            long time = System.currentTimeMillis();

            update();

            draw();

            time = (1000 / fps) - (System.currentTimeMillis() - time);
            if (time > 0) {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

            }

            if (multiplayer) {
                if (character.getLives() <= 0 || character2.getLives() <= 0) {
                    keepGoing = false;
                    server.SetKeepGoing(false);
                }
            } else if (character.getLives() <= 0) {
                keepGoing = false;
            }
        }
        Player p = db.getPlayer(playerid);
        if(character.getScore() > p.getHighscore()  ){
            db.setHighscorePlayer(playerid, character.getScore());
        }
        int currentGeoms = db.getGeoms(playerid);
        int earnedGeoms = character.getNumberOfGeoms();
        int newBalance = currentGeoms + earnedGeoms;
        db.setNewGeomBalance(newBalance, playerid);
        Frame frame = new GameOverFrame(character.getScore(),character.getNumberOfGeoms());
        frame.setVisible(true);
        gameGui.deleteGame();
        
    }

    private void init() {

        gameGui = new GameGui();
        gameWidth = gameGui.getGameWidth();
        gameHeight = gameGui.getGameHeight();

        character = db.getCharacter(characterid,playerid, gameWidth, gameHeight);

        handler = new InputHandler(gameGui.getFrame());

        if (!multiplayer) {
            switch (companionid) {
                case 1:
                    companion = db.getCompanionAutoShooter(playerid, character, enemies, bullets);
                    break;
                case 2:
                    companion = db.getCompanionLifeSaver(playerid, character);
                    System.out.println(companion.getPosX());
                    break;
                case 3:
                    companion = db.getCompanionMiner(playerid, character, mines, handler);
                    break;
                case 4:
                    companion = db.getCompanionShooter(playerid, character, handler, bullets);
                    break;
            }
           
        }
        if (multiplayer) {
            character2 = character;
            try {
                server = new Server(character, character2, bullets, enemies, geoms,powers);
                Thread t = new Thread(server);
                t.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            character2 = db.getCharacter(server.getClientCharacterId(),server.getClientPlayerId(), gameWidth, gameHeight);       
            server.setCharacter2(character2);
        }

        players = db.getPlayers();

    }

    private void update() {

        spawnEnemy();
        updatePlayerPos();
        updateBullets();
        updateEnemies();
        if (!multiplayer) {
            companion.doMove();
            companion.doSpecialAction();
        }
        
        checkPowerUpFinished();
        collisionDetection();
    }

    private void draw() {
        if (multiplayer) {
            gameGui.draw(character, character2, bullets, enemies, geoms,powers);
        } else {
            gameGui.draw(character, bullets, enemies, geoms, companion, mines,powers);
        }
    }

    private void randomSpawnGenerator() {
        int randomInt = randomGenerator.nextInt(4);
        switch (randomInt) {
            case 0:
                spawnEnemyX = 0;
                spawnEnemyY = 40 + randomGenerator.nextInt(gameHeight - 40);
                break;
            case 1:
                spawnEnemyY = 40;
                spawnEnemyX = randomGenerator.nextInt(gameWidth);
                break;
            case 2:
                spawnEnemyX = gameWidth;
                spawnEnemyY = 40 + randomGenerator.nextInt(gameHeight - 40);
                break;
            case 3:
                spawnEnemyY = gameHeight;
                spawnEnemyX = randomGenerator.nextInt(gameWidth);
                break;

        }
    }

    private void makeWaves() {
        //TODO alle waves uit de databank halen 
        /*Wave wave1 = new Wave(2,1,enemies,5);
        Wave wave2 = new Wave(3,1,enemies,5);
        Wave wave3 = new Wave(4,1,enemies,5);
        Wave wave4 = new Wave(5,1,enemies,5);
        
        waves.add(wave1);
        waves.add(wave2);
        waves.add(wave3);
        waves.add(wave4);*/

 /*for (int i = 0; i < 200; i++) {
            Wave wave = new Wave(i, 1, enemies, 5);
            waves.add(wave);
        }*/
        waves = db.getWaves();

    }

    private void spawnEnemy() {
        randomSpawnGenerator();
        Wave wave = waves.get(waveCounter);
        

        int enemyid = wave.getEnemyID();
        if (System.currentTimeMillis() - spawnTimer > wave.getSpawnRateInMs() && wave.getNumberOfEnemiesLeft() != 0 && System.currentTimeMillis() > delaytime) {

            Enemy e = db.getEnemy(enemyid, spawnEnemyX, spawnEnemyY);
            enemies.add(e);
            spawnTimer = System.currentTimeMillis();
            wave.reduceNumberOfEnemiesLeft();

        }
        if (wave.getNumberOfEnemiesLeft() == 0 && waveCounter < waves.size()) {
            int delay = wave.getDelay();
            delaytime = System.currentTimeMillis() + delay;

            // TODO use this delay (maybe an upgrade screen after 2 seconds ?
            waveCounter++;
            wave.setEnemiesLeft();

        }
        if(waveCounter == waves.size()){
            character.increaseMultiplier();
            for(Wave v : waves){
                v.increaseEnemies();
            }
            
            waveCounter = 0;
            if(multiplayer){
                character2.increaseMultiplier();
            }
        }
    }

    private void updatePlayerPos() {
        if (handler.isKeyDown(KeyEvent.VK_RIGHT)) {
            character.moveRight();

        }
        if (handler.isKeyDown(KeyEvent.VK_LEFT)) {
            character.moveLeft();

        }
        if (handler.isKeyDown(KeyEvent.VK_UP)) {
            character.moveUp();

        }
        if (handler.isKeyDown(KeyEvent.VK_DOWN)) {
            character.moveDown();

        }

        if (multiplayer) {
            if (server.isKeyRight()) {
                character2.moveRight();

            }
            if (server.isKeyLeft()) {
                character2.moveLeft();

            }
            if (server.isKeyUp()) {
                character2.moveUp();

            }
            if (server.isKeyDown()) {
                character2.moveDown();
            }
        }
    }

    private void updateBullets() {
        List<Bullet> needToRemove = new ArrayList();

        addBullets();

        for (Bullet bullet : bullets) {
            bullet.updatePos();

            if (bullet.getIsOutOfScreen()) {
                needToRemove.add(bullet);
            }
        }

        for (Bullet bullet : needToRemove) {
            bullets.remove(bullet);
        }
    }

    private void addBullets() {

        if (handler.isMouseDown(1)) {
            if (character.getLastBulletFired() + (60.0 / character.getBulletsPerMinute() * 1000) < System.currentTimeMillis()) {
                Bullet newBullet = new Bullet(character.getPosX(), character.getPosY(), handler.getEvent(1).getX(), handler.getEvent(1).getY(), character.getDamage(), gameHeight, gameWidth, character.getBulletSpeed(), character);
                bullets.add(newBullet);
                character.setLastBulletFired(System.currentTimeMillis());
            }
        }

        if (multiplayer) {
            if (server.isLeftClick()) {
                if (character2.getLastBulletFired() + (60.0 / character2.getBulletsPerMinute() * 1000) < System.currentTimeMillis()) {
                    Bullet newBullet = new Bullet(character2.getPosX(), character2.getPosY(), server.getClickLeft().getX(), server.getClickLeft().getY(), character2.getDamage(), gameHeight, gameWidth, character2.getBulletSpeed(), character2);
                    bullets.add(newBullet);
                    character2.setLastBulletFired(System.currentTimeMillis());
                }
            }
        }
    }

    private void updateEnemies() {
        boolean mayMove = true;

        for (Enemy currentEnemy : enemies) {
            mayMove = true;
            for (Enemy otherEnemy : enemies) {
                if (currentEnemy.getBounds().intersects(otherEnemy.getBounds())) {
                    double distanceCurrentEnemy = Math.sqrt((currentEnemy.getPosX() - character.getPosX()) ^ 2 + (currentEnemy.getPosY() - character.getPosY()));
                    double distanceOtherEnemy = Math.sqrt((otherEnemy.getPosX() - character.getPosX()) ^ 2 + (otherEnemy.getPosY() - character.getPosY()));

                    if (distanceCurrentEnemy > distanceOtherEnemy) {
                        mayMove = false;
                    }
                }
            }
            if (mayMove) {
                currentEnemy.updatePos(character.getPosX(), character.getPosY());
            }
        }
    }

    private void collisionDetection() {
        collisionDetection.doCollisionBetweenBulletsAndEnemys(bullets, enemies, geoms,powers);
        
        collisionDetection.doCollisionBetweenEnemiesAndCharacter(bullets, enemies, character);
        collisionDetection.doCollisionBetweenCharacterAndGeom(geoms, character);
        
        collisionDetection.doCollisionBetweenCharacterAndPowers(character, powers);

        if (multiplayer) {
            collisionDetection.doCollisionBetweenEnemiesAndCharacter(bullets, enemies, character2);
            collisionDetection.doCollisionBetweenCharacterAndGeom(geoms, character2);
            collisionDetection.doCollisionBetweenCharacterAndPowers(character2, powers);
        } else {
            collisionDetection.doCollisionBetweenEnemiesAndMine(enemies, mines);
            collisionDetection.doCollisionBetweenCharacterAndMine(mines, character);
        }
    }
    
    private void checkPowerUpFinished(){
        List<Power> powersToRemove = new ArrayList();
        
        for(Power power: powers){
            if(power.isTheEnd()){
                powersToRemove.add(power);
            }
        }
        
        for(Power p: powersToRemove){
            powers.remove(p);
        }
    }

}
