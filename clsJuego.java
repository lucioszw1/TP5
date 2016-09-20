package com.example.a41665569.tp5individual;

import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;

import org.cocos2d.actions.Scheduler;
import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.actions.interval.RotateBy;
import org.cocos2d.actions.interval.RotateTo;
import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCGridSize;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCPointSprite;
import org.cocos2d.types.CCSize;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 41665569 on 20/9/2016.
 */
public class clsJuego {
    CCGLSurfaceView _VistaDelJuego;
    CCSize PantallaDelDispositivo;
    Sprite NaveJugador;
    Sprite ImagenFondo;
    Label lblTitulodelJuego;
    Sprite NaveEnemiga;
    ArrayList<CCPointSprite> arrEnemigos;

    public clsJuego(CCGLSurfaceView VistaDelJuego) {
        _VistaDelJuego = VistaDelJuego;
    }

    public void ComenzarJuego() {
        Director.sharedDirector().attachInView(_VistaDelJuego);
        PantallaDelDispositivo = Director.sharedDirector().displaySize();
        Director.sharedDirector().runWithScene(EscenaDelJuego());
    }


    private Scene EscenaDelJuego() {
        Scene EscenaADevolver;
        EscenaADevolver = Scene.node();

        CapaDeFondo MiCapaDeFondo;
        MiCapaDeFondo = new CapaDeFondo();

        CapaDelFrente MiCapaDelFrente;
        MiCapaDelFrente = new CapaDelFrente();

        EscenaADevolver.addChild(MiCapaDeFondo, -10);
        EscenaADevolver.addChild(MiCapaDelFrente, 10);

        return EscenaADevolver;
    }

    class CapaDeFondo extends Layer {
        public CapaDeFondo() {
            PonerImagenFondo();
        }

        private void PonerImagenFondo() {
            ImagenFondo = Sprite.sprite("fondo.png");
            ImagenFondo.setPosition(PantallaDelDispositivo.width / 2, PantallaDelDispositivo.height / 2);
            ImagenFondo.runAction(ScaleBy.action(0.01f, 2.0f, 2.0f));
            super.addChild(ImagenFondo);
        }


    }

    class CapaDelFrente extends Layer {
        Sprite NaveJugador;
        Label lblNombreJuego;
        ArrayList<Sprite> arrEnemigos;
        public CapaDelFrente() {
            PonerNaveJugadorPosicionInicial();
            PonerTituloJuego();
            DetectarColisiones();
            boolean InterseccionEntreSprites;
            TimerTask TareaPonerEnemigos;
            TareaPonerEnemigos = new TimerTask() {
                @Override
                public void run() {
                    PonerUnEnemigo();
                }
            };

            Timer RelojEnemigos;
            RelojEnemigos = new Timer();
            RelojEnemigos.schedule(TareaPonerEnemigos, 0, 1000);

            this.setIsTouchEnabled(true);
            arrEnemigos=new ArrayList<Sprite>();
            TimerTask TareaVerificarImpactos;
            TareaVerificarImpactos=new TimerTask() {
                @Override
                public void run() {
                    DetectarColisiones();
                }
            };
            Timer RelojVerificarImpactos;
            RelojVerificarImpactos= new Timer();
            RelojVerificarImpactos.schedule(TareaVerificarImpactos,0,100);


        }

        private void PonerNaveJugadorPosicionInicial() {
            NaveJugador = Sprite.sprite("rocket_mini.png");

            float PosicionInicialX, PosicionInicialY;
            PosicionInicialX = PantallaDelDispositivo.width / 2;
            PosicionInicialY = PantallaDelDispositivo.height / 2;
            NaveJugador.setPosition(100, 300);
            NaveJugador.setPosition(PosicionInicialX, PosicionInicialY);
            super.addChild(NaveJugador);

        }

        private void PonerTituloJuego() {
            lblTitulodelJuego = Label.label("Andate Bauza", "Arial", 40);
            float AltoDelTitulo;
            AltoDelTitulo = lblTitulodelJuego.getHeight();
            lblTitulodelJuego.setPosition(PantallaDelDispositivo.getWidth() / 2, PantallaDelDispositivo.getHeight() - AltoDelTitulo / 2);
            super.addChild(lblTitulodelJuego);
        }

        void PonerUnEnemigo() {
            Random GeneradorDeAzar;
            GeneradorDeAzar = new Random();
            NaveEnemiga = Sprite.sprite("enemigo.gif");
            CCPoint PosicionInicial;
            PosicionInicial = new CCPoint();
            float AlturaEnemiga, AnchoEnemigo;
            AlturaEnemiga = NaveEnemiga.getHeight();
            AnchoEnemigo = NaveEnemiga.getWidth();
            PosicionInicial.y = PantallaDelDispositivo.getHeight() + AlturaEnemiga / 2;
            PosicionInicial.x = GeneradorDeAzar.nextInt((int) PantallaDelDispositivo.getWidth() - (int) AnchoEnemigo) + AnchoEnemigo / 2;
            NaveEnemiga.setPosition(PosicionInicial.x, PosicionInicial.y);
            NaveEnemiga.runAction(RotateTo.action(0.01f, 180f));
            CCPoint PosicionFinal;
            PosicionFinal = new CCPoint();
            PosicionFinal.x = PosicionInicial.x;
            PosicionFinal.y = AlturaEnemiga / 2;
            NaveEnemiga.runAction(MoveTo.action(3, PosicionFinal.x, PosicionFinal.y));
            super.addChild(NaveEnemiga);

        }

        boolean InterseccionEntreSprites(Sprite Sprite1, Sprite Sprite2) {
            boolean Devolver;
            Devolver = false;

            int Sprite1Izquierda, Sprite1Derecha, Sprite1Abajo, Sprite1Arriba;
            int Sprite2Izquierda, Sprite2Derecha, Sprite2Abajo, Sprite2Arriba;

            Sprite1Izquierda = (int) (Sprite1.getPositionX() - Sprite1.getWidth() / 2);
            Sprite1Derecha = (int) (Sprite1.getPositionX() - Sprite1.getWidth() / 2);
            Sprite1Abajo = (int) (Sprite1.getPositionY() - Sprite1.getHeight() / 2);
            Sprite1Arriba = (int) (Sprite1.getPositionY() - Sprite1.getHeight() / 2);

            Sprite2Izquierda = (int) (Sprite2.getPositionX() - Sprite2.getWidth() / 2);
            Sprite2Derecha = (int) (Sprite2.getPositionX() - Sprite2.getWidth() / 2);
            Sprite2Abajo = (int) (Sprite2.getPositionY() - Sprite2.getHeight() / 2);
            Sprite2Arriba = (int) (Sprite2.getPositionY() - Sprite2.getHeight() / 2);

            if (EstaEntre(Sprite1Izquierda,Sprite2Izquierda,Sprite2Derecha)&& EstaEntre(Sprite1Abajo,Sprite2Abajo,Sprite2Arriba)){
            Devolver=true;
            }

            if (EstaEntre(Sprite1Izquierda,Sprite2Izquierda,Sprite2Derecha)&& EstaEntre(Sprite1Arriba,Sprite2Abajo,Sprite2Arriba)){
                Devolver=true;
            }

            if (EstaEntre(Sprite1Derecha,Sprite2Izquierda,Sprite2Derecha)&& EstaEntre(Sprite1Abajo,Sprite2Abajo,Sprite2Arriba)){
                Devolver=true;
            }

            if (EstaEntre(Sprite1Derecha,Sprite2Izquierda,Sprite2Derecha)&& EstaEntre(Sprite1Arriba,Sprite2Abajo,Sprite2Arriba)){
                Devolver=true;
            }

            if (EstaEntre(Sprite2Izquierda,Sprite1Izquierda,Sprite1Derecha)&& EstaEntre(Sprite2Arriba,Sprite1Abajo,Sprite1Arriba)){
                Devolver=true;
            }
            if (EstaEntre(Sprite2Izquierda,Sprite1Izquierda,Sprite1Derecha)&& EstaEntre(Sprite2Abajo,Sprite1Abajo,Sprite1Arriba)){
                Devolver=true;
            }
            if (EstaEntre(Sprite2Derecha,Sprite1Izquierda,Sprite1Derecha)&& EstaEntre(Sprite2Arriba,Sprite1Abajo,Sprite1Arriba)){
                Devolver=true;
            }
            if (EstaEntre(Sprite2Derecha,Sprite1Izquierda,Sprite1Derecha)&& EstaEntre(Sprite2Abajo,Sprite1Abajo,Sprite1Arriba)){
                Devolver=true;
            }

            return Devolver;
        }

         boolean EstaEntre(int NumeroAComparar, int NumeroMayor, int NumeroMenor)
         {
             boolean Devolver;
             if(NumeroMenor>NumeroMayor)
             {
                 int Auxiliar;
                 Auxiliar = NumeroMayor;
                 NumeroMayor = NumeroMenor;
                 NumeroMenor = Auxiliar;
             }

             if(NumeroAComparar >= NumeroMenor && NumeroAComparar<=NumeroMayor)
             {
                 Devolver = true;
             } else{
                 Devolver = false;
             }
             return Devolver;
         }


            void DetectarColisiones(){
                boolean HuboAlgunaColision;
                HuboAlgunaColision = false;
//                for (Sprite UnEnemigoAVerificar: arrEnemigos){
                 //   if (InterseccionEntreSprites(NaveJugador,UnEnemigoAVerificar)){
                 //   HuboAlgunaColision = true;
                  //  }
  //              }
                if (HuboAlgunaColision==true){

                }else{

                }
            }


            public boolean ccTouchesBegan(MotionEvent event)
            {
                Log.d("Toque Comienza","X:"+event.getX()+"-Y"+event.getY());
              return true;
            }


            public boolean ccTouchesMoved(MotionEvent event)
            {
                Log.d("Toque Mueve","X:"+event.getX()+"-Y"+event.getY());
                MoverNaveJugador(event.getX(),PantallaDelDispositivo.getHeight() - event.getY());
                return true;
            }
            void  MoverNaveJugador(float DestinoX, Float DestinoY)
            {
                float MovimientoHorizontal,MovimientoVertical,SuavizadorDeMovimiento;
                MovimientoHorizontal = DestinoX - PantallaDelDispositivo.getWidth()/2;
                MovimientoVertical = DestinoY - PantallaDelDispositivo.getHeight()/2;

                SuavizadorDeMovimiento=20;
                MovimientoHorizontal=MovimientoHorizontal/SuavizadorDeMovimiento;
                MovimientoVertical=MovimientoVertical/MovimientoVertical;
                NaveJugador.setPosition(NaveJugador.getPositionX()+ MovimientoHorizontal,NaveJugador.getPositionY()+MovimientoVertical);

            }


            public boolean ccTouchesEnded(MotionEvent event)
            {
                Log.d("Toque Termina","X:"+event.getX()+"-Y"+event.getY());

                return true;
            }


        }








    }

