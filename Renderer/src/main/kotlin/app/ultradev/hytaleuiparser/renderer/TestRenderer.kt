package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.Validator
import app.ultradev.hytaleuiparser.renderer.command.CommandApplicator
import app.ultradev.hytaleuiparser.renderer.command.UICommand
import app.ultradev.hytaleuiparser.source.AssetSources
import app.ultradev.hytaleuiparser.source.DirectoryAssetSource
import java.awt.BorderLayout
import java.awt.Dimension
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel

object TestRenderer {
    val source = AssetSources.getAssetsZipSource(patchline = "pre-release")

    val testCommands = listOf(
        UICommand(UICommand.Type.Append, null, null, "Pages/BarterPage.ui"),
        UICommand(UICommand.Type.Set, "#ShopTitle.Text", "{\"0\": {\"MessageId\": \"server.barter.kweebec_merchant.title\", \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Set, "#RefreshTimer.Text", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.restocksInDays\", \"Params\": {\"days\": 3}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Clear, "#TradeGrid", null, null),
        UICommand(UICommand.Type.Append, "#TradeGrid", null, "Pages/BarterTradeRow.ui"),
        UICommand(UICommand.Type.Set, "#TradeGrid[0] #OutputSlot.ItemId", "{\"0\": \"Ingredient_Spices\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[0] #OutputQuantity.Text", "{\"0\": \"3\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[0] #InputSlot.ItemId", "{\"0\": \"Ingredient_Life_Essence\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[0] #InputQuantity.Text", "{\"0\": \"20\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[0] #InputSlotBorder.Background", "{\"0\": \"#5a2a2a\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[0] #HaveNeedLabel.Text", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.quantityStock\", \"Params\": {\"count\": 0}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[0] #HaveNeedLabel.Style.TextColor", "{\"0\": \"#962f2f\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[0] #Stock.TextSpans", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.inStock\", \"Params\": {\"count\": 10}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Append, "#TradeGrid", null, "Pages/BarterTradeRow.ui"),
        UICommand(UICommand.Type.Set, "#TradeGrid[1] #OutputSlot.ItemId", "{\"0\": \"Ingredient_Salt\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[1] #OutputQuantity.Text", "{\"0\": \"3\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[1] #InputSlot.ItemId", "{\"0\": \"Ingredient_Life_Essence\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[1] #InputQuantity.Text", "{\"0\": \"20\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[1] #InputSlotBorder.Background", "{\"0\": \"#5a2a2a\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[1] #HaveNeedLabel.Text", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.quantityStock\", \"Params\": {\"count\": 0}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[1] #HaveNeedLabel.Style.TextColor", "{\"0\": \"#962f2f\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[1] #Stock.TextSpans", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.inStock\", \"Params\": {\"count\": 10}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Append, "#TradeGrid", null, "Pages/BarterTradeRow.ui"),
        UICommand(UICommand.Type.Set, "#TradeGrid[2] #OutputSlot.ItemId", "{\"0\": \"Ingredient_Dough\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[2] #OutputQuantity.Text", "{\"0\": \"3\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[2] #InputSlot.ItemId", "{\"0\": \"Ingredient_Life_Essence\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[2] #InputQuantity.Text", "{\"0\": \"20\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[2] #InputSlotBorder.Background", "{\"0\": \"#5a2a2a\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[2] #HaveNeedLabel.Text", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.quantityStock\", \"Params\": {\"count\": 0}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[2] #HaveNeedLabel.Style.TextColor", "{\"0\": \"#962f2f\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[2] #Stock.TextSpans", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.inStock\", \"Params\": {\"count\": 10}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Append, "#TradeGrid", null, "Pages/BarterTradeRow.ui"),
        UICommand(UICommand.Type.Set, "#TradeGrid[3] #OutputSlot.ItemId", "{\"0\": \"Plant_Crop_Berry_Block\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[3] #OutputQuantity.Text", "{\"0\": \"\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[3] #InputSlot.ItemId", "{\"0\": \"Ingredient_Life_Essence\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[3] #InputQuantity.Text", "{\"0\": \"30\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[3] #InputSlotBorder.Background", "{\"0\": \"#5a2a2a\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[3] #HaveNeedLabel.Text", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.quantityStock\", \"Params\": {\"count\": 0}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[3] #HaveNeedLabel.Style.TextColor", "{\"0\": \"#962f2f\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[3] #Stock.TextSpans", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.inStock\", \"Params\": {\"count\": 15}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Append, "#TradeGrid", null, "Pages/BarterTradeRow.ui"),
        UICommand(UICommand.Type.Set, "#TradeGrid[4] #OutputSlot.ItemId", "{\"0\": \"Food_Kebab_Mushroom\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[4] #OutputQuantity.Text", "{\"0\": \"\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[4] #InputSlot.ItemId", "{\"0\": \"Ingredient_Life_Essence\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[4] #InputQuantity.Text", "{\"0\": \"15\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[4] #InputSlotBorder.Background", "{\"0\": \"#5a2a2a\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[4] #HaveNeedLabel.Text", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.quantityStock\", \"Params\": {\"count\": 0}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[4] #HaveNeedLabel.Style.TextColor", "{\"0\": \"#962f2f\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[4] #Stock.TextSpans", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.inStock\", \"Params\": {\"count\": 6}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Append, "#TradeGrid", null, "Pages/BarterTradeRow.ui"),
        UICommand(UICommand.Type.Set, "#TradeGrid[5] #OutputSlot.ItemId", "{\"0\": \"Food_Kebab_Meat\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[5] #OutputQuantity.Text", "{\"0\": \"\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[5] #InputSlot.ItemId", "{\"0\": \"Ingredient_Life_Essence\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[5] #InputQuantity.Text", "{\"0\": \"15\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[5] #InputSlotBorder.Background", "{\"0\": \"#5a2a2a\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[5] #HaveNeedLabel.Text", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.quantityStock\", \"Params\": {\"count\": 0}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[5] #HaveNeedLabel.Style.TextColor", "{\"0\": \"#962f2f\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[5] #Stock.TextSpans", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.inStock\", \"Params\": {\"count\": 7}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Append, "#TradeGrid", null, "Pages/BarterTradeRow.ui"),
        UICommand(UICommand.Type.Set, "#TradeGrid[6] #OutputSlot.ItemId", "{\"0\": \"Food_Pie_Apple\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[6] #OutputQuantity.Text", "{\"0\": \"\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[6] #InputSlot.ItemId", "{\"0\": \"Ingredient_Life_Essence\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[6] #InputQuantity.Text", "{\"0\": \"50\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[6] #InputSlotBorder.Background", "{\"0\": \"#5a2a2a\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[6] #HaveNeedLabel.Text", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.quantityStock\", \"Params\": {\"count\": 0}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[6] #HaveNeedLabel.Style.TextColor", "{\"0\": \"#962f2f\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[6] #Stock.TextSpans", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.inStock\", \"Params\": {\"count\": 8}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Append, "#TradeGrid", null, "Pages/BarterTradeRow.ui"),
        UICommand(UICommand.Type.Set, "#TradeGrid[7] #OutputSlot.ItemId", "{\"0\": \"Food_Pie_Pumpkin\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[7] #OutputQuantity.Text", "{\"0\": \"\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[7] #InputSlot.ItemId", "{\"0\": \"Ingredient_Life_Essence\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[7] #InputQuantity.Text", "{\"0\": \"50\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[7] #InputSlotBorder.Background", "{\"0\": \"#5a2a2a\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[7] #HaveNeedLabel.Text", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.quantityStock\", \"Params\": {\"count\": 0}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[7] #HaveNeedLabel.Style.TextColor", "{\"0\": \"#962f2f\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[7] #Stock.TextSpans", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.inStock\", \"Params\": {\"count\": 8}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Append, "#TradeGrid", null, "Pages/BarterTradeRow.ui"),
        UICommand(UICommand.Type.Set, "#TradeGrid[8] #OutputSlot.ItemId", "{\"0\": \"Food_Salad_Caesar\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[8] #OutputQuantity.Text", "{\"0\": \"\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[8] #InputSlot.ItemId", "{\"0\": \"Ingredient_Life_Essence\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[8] #InputQuantity.Text", "{\"0\": \"50\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[8] #InputSlotBorder.Background", "{\"0\": \"#5a2a2a\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[8] #HaveNeedLabel.Text", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.quantityStock\", \"Params\": {\"count\": 0}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[8] #HaveNeedLabel.Style.TextColor", "{\"0\": \"#962f2f\"}", null),
        UICommand(UICommand.Type.Set, "#TradeGrid[8] #Stock.TextSpans", "{\"0\": {\"MessageId\": \"server.barter.customUI.barterPage.inStock\", \"Params\": {\"count\": 7}, \"Bold\": null, \"Italic\": null, \"Monospace\": null, \"Underline\": null}}", null),

        )

    @JvmStatic
    fun main(args: Array<String>) {
        val frame = JFrame("Hytale UI Renderer")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.size = Dimension(800, 600)

//        val assets = AssetSources.parseUIFiles(source)
//        val validator = Validator(assets, assetSource = source)

        val rootUIElement = CommandApplicator(source)(testCommands)

//        val testPage = validator.validateRoot("Common/UI/Custom/Pages/PrefabSavePage.ui") ?: error("Failed to validate page")
//
//        val rootUIElement = UITransformer.transform(testPage)


        val backgroundImage = ImageIO.read(javaClass.getResourceAsStream("/background.png"))

        val parent = JPanel(BorderLayout())
        parent.add(HytaleUIPanel(rootUIElement, backgroundImage, source), BorderLayout.CENTER)

        frame.contentPane = parent

        frame.isVisible = true
    }
}