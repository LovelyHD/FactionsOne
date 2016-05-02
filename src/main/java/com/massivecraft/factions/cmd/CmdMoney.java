package com.massivecraft.factions.cmd;

import com.massivecraft.factions.P;

public class CmdMoney extends FCommand {

    public CmdMoneyBalance cmdMoneyBalance = new CmdMoneyBalance();
    public CmdMoneyDeposit cmdMoneyDeposit = new CmdMoneyDeposit();
    public CmdMoneyWithdraw cmdMoneyWithdraw = new CmdMoneyWithdraw();
    public CmdMoneyTransferFf cmdMoneyTransferFf = new CmdMoneyTransferFf();
    public CmdMoneyTransferFp cmdMoneyTransferFp = new CmdMoneyTransferFp();
    public CmdMoneyTransferPf cmdMoneyTransferPf = new CmdMoneyTransferPf();

    public CmdMoney() {
        super();
        aliases.add("money");

        // this.requiredArgs.add("");
        // this.optionalArgs.put("","")
        isMoneyCommand = true;

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;

        setHelpShort("faction money commands");
        helpLong.add(p.txt.parseTags("<i>The faction money commands."));

        addSubCommand(cmdMoneyBalance);
        addSubCommand(cmdMoneyDeposit);
        addSubCommand(cmdMoneyWithdraw);
        addSubCommand(cmdMoneyTransferFf);
        addSubCommand(cmdMoneyTransferFp);
        addSubCommand(cmdMoneyTransferPf);
    }

    @Override
    public void perform() {
        commandChain.add(this);
        P.p.cmdAutoHelp.execute(sender, args, commandChain);
    }

}
