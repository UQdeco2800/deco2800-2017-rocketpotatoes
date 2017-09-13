 package com.deco2800.potatoes.entities;

    /**
     *
     *In game time system
     *
     */
    public abstract class GameTime implements Tickable {
        private int CurrentTime = 0;
        private int CurrentDay = 0;
        private boolean DayTime = true;

        /**
         * Default constructor
         */
            public GameTime()    {
            }

        /**
         * @return the current in game time
         */
            public int getCurrentTime() { return CurrentTime; }

        /**
         * Resets the Current Time.
         * @param CurrentTime
         */
            public void ResetCurrentTime(int CurrentTime) { this.CurrentTime = 0; }

        /**
         * Sets the Current Game Time.
         * @param CurrentTime
         */
            public void SetCurrentTime(int CurrentTime) { this.CurrentTime = CurrentTime; }


        /**
         * Increases the Current Game Time.
         * @param Tick
         */
            public void onTick(int Tick){
                this.SetCurrentTime((int) (this.getCurrentTime() + 0.01));
            }

        /**
         *  Transition into night time
         */
            public void NightTime(int CurrentTime, boolean DayTime){
            while(CurrentTime >= 12){
                DayTime = false;
            }
        }

        /**
         * Rolling over into next day
         */
           public void nextDay(int CurrentTime, int CurrentDay){
                if(CurrentTime == 24){
                    CurrentDay += 1;
                    CurrentTime = 0;
                }
           }

        /**
         * @return the current In Game Day.
         */
            public int getCurrentDay() { return CurrentDay; }

        /**
         * Resets the Current Day.
         * @param CurrentDay
         */
            public void ResetCurrentDay(int CurrentDay) { this.CurrentDay = 0; }

        /**
         * Sets the Current Game Day.
         * @param CurrentDay
         */
            public void SetCurrentDay(int CurrentDay) { this.CurrentDay = CurrentDay; }

    }
