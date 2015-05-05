package models;

import play.i18n.Messages;

/**
 * Created by lukas on 05.05.2015.
 */
public enum Avatars {

    ALDRICHKILLIAN(Messages.get("aldrich-killian")),
    BEETLE(Messages.get("beetle")),
    BLACKWIDOW(Messages.get("blackwidow")),
    CAPTAINAMERICA(Messages.get("captainamerica")),
    CYCLOPS(Messages.get("cyclops")),
    DEADPOOL(Messages.get("deadpool")),
    DOCTORDOOM(Messages.get("doctordoom")),
    DOCTOROCTOPUS(Messages.get("doctoroctopus")),
    DRAX(Messages.get("drax")),
    ELECTRO(Messages.get("electro")),
    EXTREMISSOLDIER(Messages.get("extremissoldier")),
    FALCON(Messages.get("falcon")),
    GAMORA(Messages.get("gamora")),
    GREENGOBLIN(Messages.get("greengoblin")),
    GROOT(Messages.get("groot")),
    HAWKEYE(Messages.get("hawkeye")),
    HULK(Messages.get("hulk")),
    HYDRAHENCHMAN(Messages.get("hydrahenchman")),
    IRONFIST(Messages.get("ironfist")),
    IRONMAN(Messages.get("ironman")),
    JJONAHJAMESON(Messages.get("jjonahjameson")),
    LOKI(Messages.get("loki")),
    MAGNETO(Messages.get("magneto")),
    MARYJANEWATSON(Messages.get("maryjanewatson")),
    MODOK(Messages.get("modok")),
    NEBULA(Messages.get("nebula")),
    NICKFURY(Messages.get("nickfury")),
    NOVA(Messages.get("nova")),
    PEPPERPOTTS(Messages.get("pepperpotts")),
    POWERMAN(Messages.get("powerman")),
    REDSKULL(Messages.get("redskull")),
    ROCKET(Messages.get("rocket")),
    RONAN(Messages.get("ronan")),
    SPIDERMAN(Messages.get("spiderman")),
    STARLORD(Messages.get("starlord")),
    TASKMASTER(Messages.get("taskmaster")),
    THEMANDARIN(Messages.get("themandarin")),
    THOR(Messages.get("thor")),
    TONYSTARK(Messages.get("tonystark")),
    VENOM(Messages.get("venom")),
    WARMACHINE(Messages.get("warmachine")),
    WOLVERINE(Messages.get("wolverine"));




    String text;

    Avatars(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
