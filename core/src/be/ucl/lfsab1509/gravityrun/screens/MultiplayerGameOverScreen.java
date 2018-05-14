package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

class MultiplayerGameOverScreen extends AbstractGameOverScreen {

    MultiplayerGameOverScreen(GravityRun gravityRun, int myFinalScore, int opponentFinalScore) {
        super(gravityRun);

        title.setText(game.i18n.format(myFinalScore >= opponentFinalScore ? "winner" : "game_over"));

        label1.setText(game.i18n.format("final_score", myFinalScore));
        label2.setText(game.i18n.format("opponent_final_score", opponentFinalScore));
    }

    @Override
    void backToMenu() {
        disconnect();
    }

    @Override
    void handleInput() {
        if (MultiplayerConnectionScreen.isClient && MultiplayerConnectionScreen.ready) {
            AbstractMultiPlayScreen abstractMultiPlayScreen = game.user.getMultiMode() == 0
                    ? new MultiPlayFirstModeScreen(game)
                    : new MultiPlaySecondModeScreen(game);

            setMultiPlayScreen(abstractMultiPlayScreen);
            screenManager.set(abstractMultiPlayScreen);
        }
    }

    @Override
    public boolean isHost() {
        return bluetoothManager.isHost() && isConnected();
    }

    @Override
    void replayGame() {
        write(AbstractMultiPlayScreen.GAME_INFO,
                game.user.getMultiLives(),
                game.user.getMultiIndexSelected(),
                game.user.getMultiMode());

        AbstractMultiPlayScreen multiPlayScreen = game.user.getMultiMode() == 0
                ? new MultiPlayFirstModeScreen(game)
                : new MultiPlaySecondModeScreen(game);

        setMultiPlayScreen(multiPlayScreen);
        screenManager.set(multiPlayScreen);
    }
}
