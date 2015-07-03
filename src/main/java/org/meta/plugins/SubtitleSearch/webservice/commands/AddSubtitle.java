package org.meta.plugins.SubtitleSearch.webservice.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

import org.meta.dht.DHTOperation;
import org.meta.model.DataFile;
import org.meta.model.DataString;
import org.meta.model.MetaData;
import org.meta.model.MetaProperty;
import org.meta.model.ModelFactory;
import org.meta.model.Search;
import org.meta.model.Searchable;
import org.meta.plugin.AbstractPluginWebServiceControler;
import org.meta.plugin.webservice.AbstractWebService;
import org.meta.plugin.webservice.forms.InterfaceDescriptor;
import org.meta.plugin.webservice.forms.fields.TextInput;
import org.meta.plugin.webservice.forms.fields.TextOutput;
import org.meta.plugin.webservice.forms.submit.SelfSubmitButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddSubtitle extends AbstractWebService{

    InterfaceDescriptor  initialDescriptor   = null;
    TextOutput           output              = null;
    ModelFactory         factory             = null;
    ArrayList<DataString>results             = null;
    TextInput            source              = null;
    TextInput            result              = null;
    TextInput            description         = null;
    
    private Logger logger = LoggerFactory.getLogger(AddSubtitle.class);
    
    public AddSubtitle(AbstractPluginWebServiceControler controler){
        super(controler);
        factory = this.controler.getModel().getFactory();
        results = new ArrayList<DataString>();
        source = new TextInput("path", "Path to the movie");
        rootColumn.addChild(source);

        result = new TextInput("pathS", "Path to the subtitle");
        rootColumn.addChild(result);

        description = new TextInput("description", "Description of the subtitle");
        rootColumn.addChild(description);

        output = new TextOutput("result", "Result");
        rootColumn.addChild(new SelfSubmitButton("submitToMe", "add a subtitle"));
        rootColumn.addChild(output);

        initialDescriptor = new InterfaceDescriptor(rootColumn);
        super.controler.getModel().getFactory();
    }

    @Override
    public void executeCommand(Map<String, String[]> map) {
        String source = getParameter(this.source.getId(), map);
        String result = getParameter(this.result.getId(), map);
        String desc   = getParameter(this.description.getId(), map);

        if(source != null && result != null){
            File fSource = new File(source);
            File fResult = new File(result);
            
            if(fSource.exists() && fResult.exists())
                processCreation(fResult, fSource, desc);
            else{
                output.flush();
                output.append("Please set valid pathes");
            }

        }else{
            output.flush();
            output.append("Please set both pathes");
        }
    }

    /**
     * Process DataFile creation for source and result.
     * Process Search creation.
     * 
     *  
     *  process a save action to the search
     *  process a DHT push on the search
     *  process a push action on the content
     *  
     * @param fResult
     * @param fSource
     * @param description
     */
    private void processCreation(File fResult, File fSource, String description){
        //Create the empty search 
        TreeSet<MetaProperty> properties = new TreeSet<MetaProperty>();
        properties.add(new MetaProperty("st", "fr"));

        MetaData metaData = factory.createMetaData(properties);
        DataFile src = factory.createDataFile(fSource);

        Search newSearch = factory.createSearch(src, metaData, null);

        DataFile newResult = factory.createDataFile(fResult);
        
        newSearch = super.updateSearch(newSearch, newResult);
        /*
         * Update description of newResult, at this point if newResult may have
         * changed is reference
         */
        if(description != null){
            ArrayList<MetaProperty> descriptions  = newResult.getDescription();
            descriptions.add(new MetaProperty("description", description));
        }
        super.saveAndPush(newSearch);
        super.onlyPush(newResult);
    }

    @Override
    public void applySmallUpdate() {}
    @Override
    public void callbackSuccess(ArrayList<Searchable> results) {}
    @Override
    public void callbackFailure(String failureMessage) {}
    
    @Override
    protected void callbackFailedToPush(DHTOperation operation, Searchable s) {
        output.append("Fail to push "+s.getHash()+" "+operation.getFailureMessage());
    }
    @Override
    protected void callbackSuccessToPush(DHTOperation operation, Searchable s) {
        output.append("Success to push "+s.getHash());
    }
}
