<#import "template.ftl" as layout />

<@layout.noauthentication>
    <section>
        <div class="container">
            <p>
                An Air Quality Index application using Kotlin and Ktor.
            </p>
            <li>
            <b>
                Id  |   City   |   Date   | PM10 Avg | PM10 Max | PM10 Min
            </b>
            </li>
             <ul>
                <#list headers as header>
                    <li>
                        ${header?join("  |  ")}
                    </li>
                </#list>
            </ul>
        </div>
    </section>

</@layout.noauthentication>