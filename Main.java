
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.util.Hashtable;

public class Main {

    public static void main(String[] args) {
        try
        {
            // Set up the environment for creating the initial context, change variables to match your organization
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://xrghjkl.susedu.hegn.us:3268"); //this has to match your organization ldap url
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, "SUSEDU\\johndoe"); //your username
            env.put(Context.SECURITY_CREDENTIALS, "MyPass123456"); //your password

            // Create the initial context
            DirContext ctx = new InitialDirContext(env);

            System.out.println("Connection Successful.");

            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            //member attribute will tell us who is in the group.
            String[] attrIDs = {"member"};
            searchControls.setReturningAttributes(attrIDs);

            String groupName="AWS-eng-data";
            //LDAP Query
            String searchFilter = "(&(objectCategory=group)(CN="+groupName+"))";

            //do a search
            NamingEnumeration<SearchResult> results = ctx.search("",searchFilter,searchControls);

            System.out.println("Search result for group:"+groupName);

            while (results.hasMore()) {
                SearchResult rslt = (SearchResult) results.next();
                Attributes attrs = rslt.getAttributes();

                String[] members = attrs.get("member").toString().split("CN");

                for(String val: members) {
                    System.out.println("CN"+val);
                }
            }

            ctx.close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
