package cn.windery.sentinel.slots;

public abstract class AbstractRule implements Rule {


    protected String resource;
    protected String type;


    public AbstractRule(String resource, String type) {
        this.resource = resource;
        this.type = type;
    }


    @Override
    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
