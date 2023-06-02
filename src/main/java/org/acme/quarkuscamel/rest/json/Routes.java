/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.acme.quarkuscamel.rest.json;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Camel route definitions.
 */
public class Routes extends RouteBuilder {
    private final Set<Fruta> frutas = Collections.synchronizedSet(new LinkedHashSet<>());
    private final Set<Legumbre> legumbres = Collections.synchronizedSet(new LinkedHashSet<>());

    public Routes() {

        this.frutas.add(new Fruta("Manzana", "Fruta de invierno"));
        this.frutas.add(new Fruta("Pineapple", "Fruta Tropical"));

        this.legumbres.add(new Legumbre("Frijol", "cosechado dede una vaina"));
        this.legumbres.add(new Legumbre("Papa", "cosechado de la tierra"));
    }

    @Override
    public void configure() throws Exception {

        restConfiguration().bindingMode(RestBindingMode.json);

        rest("/frutas")
                .get()
                .to("direct:getFrutas")

                .post()
                .type(Fruta.class)
                .to("direct:addFruta");

        rest("/legumbres")
                .get()
                .to("direct:getLegumbres");

        from("direct:getFrutas")
                .setBody().constant(frutas);

        from("direct:addFruta")
                .process().body(Fruta.class, frutas::add)
                .setBody().constant(frutas);

        from("direct:getLegumbres")
                .setBody().constant(legumbres);
    }
}
