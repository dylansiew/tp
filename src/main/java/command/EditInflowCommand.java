package command;

import customexceptions.CategoryNotFoundException;
import customexceptions.IncorrectCommandSyntaxException;
import financialtransactions.Inflow;
import financialtransactions.TransactionManager;

public class EditInflowCommand extends BaseCommand {
    private int inflowIndex = -1;
    private String inflowName = null;
    private double inflowAmount = 0;
    private String inflowDate = null;
    private String inflowTime = null;
    private String inflowCategory = null;
    private Inflow updatedInflow;
    private TransactionManager manager;

    public EditInflowCommand(String[] commandParts) {
        super(false, commandParts);
    }

    public void setManager(TransactionManager manager) {
        this.manager = manager;
    }

    @Override
    public void createTransaction() throws IncorrectCommandSyntaxException {
        /* Iterates through the parts of the original command string that checks and updates
        relevant inflow information. */
        for (int i = 1; i < commandParts.length; i++) {
            String part = commandParts[i];
            if (part.startsWith("i/")) {
                inflowIndex = Integer.parseInt(part.substring(2));
            } else if (part.startsWith("n/")) {
                inflowName = part.substring(2);
            } else if (part.startsWith("a/")) {
                inflowAmount = Double.parseDouble(part.substring(2));
                if (inflowAmount <= 0) {
                    throw new IllegalArgumentException("Sorry, inflow amount must be positive.");
                }
            } else if (part.startsWith("d/")) {
                inflowDate = part.substring(2);
            } else if (part.startsWith("t/")) {
                inflowTime = part.substring(2);
            } else if (part.startsWith("c/")) {
                inflowCategory = part.substring(2);
            } else {
                throw new IncorrectCommandSyntaxException(commandParts[0]);
            }
        }
        assert inflowIndex != -1 : "inflow index should exist";
        assert inflowCategory != null : "inflowCategory should not be null";
        try {
            updatedInflow.setCategory(inflowCategory);
            inflow = manager.getNthInflowFromList(inflowIndex);
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Sorry. " + e.getMessage());
        }
        updatedInflow = new Inflow(inflowName, inflowAmount, inflowDate + " " + inflowTime);
    }

    public String execute(TransactionManager manager) throws Exception {
        if (!canExecute) {
            return "Sorry, inflow not edited.";
        }
        manager.editInflow(inflowIndex, updatedInflow);
        return "Ok. Edited inflow";
    }
}
