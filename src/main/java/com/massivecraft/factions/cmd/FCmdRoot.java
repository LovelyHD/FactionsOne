package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import java.util.Collections;

public class FCmdRoot extends FCommand {

    public CmdAccess cmdAccess = new CmdAccess();
    public CmdLeader cmdLeader = new CmdLeader();
    public CmdAutoClaim cmdAutoClaim = new CmdAutoClaim();
    public CmdAdmin cmdBypass = new CmdAdmin();
    public CmdClaim cmdClaim = new CmdClaim();
    public CmdConfig cmdConfig = new CmdConfig();
    public CmdCreate cmdCreate = new CmdCreate();
    public CmdDeinvite cmdDeinvite = new CmdDeinvite();
    public CmdDemote cmdDemote = new CmdDemote();
    public CmdDescription cmdDescription = new CmdDescription();
    public CmdDisband cmdDisband = new CmdDisband();
    public CmdFlag cmdFlag = new CmdFlag();
    public CmdHome cmdHome = new CmdHome();
    public CmdInvite cmdInvite = new CmdInvite();
    public CmdJoin cmdJoin = new CmdJoin();
    public CmdKick cmdKick = new CmdKick();
    public CmdLeave cmdLeave = new CmdLeave();
    public CmdList cmdList = new CmdList();
    public CmdLock cmdLock = new CmdLock();
    public CmdMap cmdMap = new CmdMap();
    public CmdOfficer cmdOfficer = new CmdOfficer();
    public CmdMoney cmdMoney = new CmdMoney();
    public CmdOpen cmdOpen = new CmdOpen();
    public CmdPerm cmdPerm = new CmdPerm();
    public CmdPower cmdPower = new CmdPower();
    public CmdPowerBoost cmdPowerBoost = new CmdPowerBoost();
    public CmdPromote cmdPromote = new CmdPromote();
    public CmdRelationAlly cmdRelationAlly = new CmdRelationAlly();
    public CmdRelationEnemy cmdRelationEnemy = new CmdRelationEnemy();
    public CmdRelationNeutral cmdRelationNeutral = new CmdRelationNeutral();
    public CmdRelationTruce cmdRelationTruce = new CmdRelationTruce();
    public CmdReload cmdReload = new CmdReload();
    public CmdSaveAll cmdSaveAll = new CmdSaveAll();
    public CmdSeeChunk cmdSeeChunks = new CmdSeeChunk();
    public CmdSethome cmdSethome = new CmdSethome();
    public CmdShow cmdShow = new CmdShow();
    public CmdTag cmdTag = new CmdTag();
    public CmdTitle cmdTitle = new CmdTitle();
    public CmdUnclaim cmdUnclaim = new CmdUnclaim();
    public CmdUnclaimall cmdUnclaimall = new CmdUnclaimall();
    public CmdVersion cmdVersion = new CmdVersion();

    public FCmdRoot() {
        super();
        aliases.addAll(Conf.baseCommandAliases);
        aliases.removeAll(Collections.singletonList(null)); // remove any
        // nulls
        // from
        // extra
        // commas

        // this.requiredArgs.add("");
        // this.optionalArgs.put("","")
        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;

        disableOnLock = false;

        setHelpShort("The faction base command");
        helpLong.add(p.txt.parseTags("<i>This command contains all faction stuff."));

        addSubCommand(P.p.cmdAutoHelp);
        addSubCommand(cmdList);
        addSubCommand(cmdShow);
        addSubCommand(cmdPower);
        addSubCommand(cmdJoin);
        addSubCommand(cmdLeave);
        addSubCommand(cmdHome);
        addSubCommand(cmdCreate);
        addSubCommand(cmdSethome);
        addSubCommand(cmdTag);
        addSubCommand(cmdDemote);
        addSubCommand(cmdDescription);
        addSubCommand(cmdPerm);
        addSubCommand(cmdFlag);
        addSubCommand(cmdInvite);
        addSubCommand(cmdDeinvite);
        addSubCommand(cmdOpen);
        addSubCommand(cmdMoney);
        addSubCommand(cmdClaim);
        addSubCommand(cmdAutoClaim);
        addSubCommand(cmdUnclaim);
        addSubCommand(cmdUnclaimall);
        addSubCommand(cmdAccess);
        addSubCommand(cmdKick);
        addSubCommand(cmdOfficer);
        addSubCommand(cmdLeader);
        addSubCommand(cmdTitle);
        addSubCommand(cmdMap);
        addSubCommand(cmdSeeChunks);
        addSubCommand(cmdDisband);
        addSubCommand(cmdRelationAlly);
        addSubCommand(cmdRelationEnemy);
        addSubCommand(cmdRelationNeutral);
        addSubCommand(cmdRelationTruce);
        addSubCommand(cmdBypass);
        addSubCommand(cmdPowerBoost);
        addSubCommand(cmdPromote);
        addSubCommand(cmdLock);
        addSubCommand(cmdReload);
        addSubCommand(cmdConfig);
        addSubCommand(cmdSaveAll);
        addSubCommand(cmdVersion);
    }

    @Override
    public void perform() {
        commandChain.add(this);
        P.p.cmdAutoHelp.execute(sender, args, commandChain);
    }

}
