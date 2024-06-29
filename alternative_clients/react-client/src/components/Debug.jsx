import {useState} from 'react';

export default function Debug({info}) {

    const [selectedRef, setSelectedRef] = useState(Object.keys(info.env).length > 0 ? Object.keys(info.env)[0] : "")

    const selectChildrens = [];

    const finalRef = (!info.env[selectedRef]) ? (Object.keys(info.env).length > 0 ? Object.keys(info.env)[0] : "") : selectedRef

    const finalRefScriptInfo = (info.env[finalRef] && Object.keys(info.env[finalRef].scripts).length > 0) ?
    Object.keys(info.env[finalRef].scripts).map(scriptName => {
        return `\t-${scriptName}:\n\t\tNb params: ${info.env[finalRef].scripts[scriptName].nbParams}\n\t\tPrototpye: ${info.env[finalRef].scripts[scriptName].proto}\n\t\tExpression: ${info.env[finalRef].scripts[scriptName].expr.replaceAll("\n", "\n\t\t\t")}`
    }).join("\n").replace(/^/, "\nScripts:\n") : ""

    const refInfo = info.env[finalRef] ? `Type: ${info.env[finalRef].className}\nPrimitives:\n\t- ${info.env[finalRef].primitives.join("\n\t- ")}${finalRefScriptInfo}` : ""

    Object.keys(info.env).forEach((key) => {
        selectChildrens.push(<option key={key} value={key}>{key}</option>)
    })

    return <>
        <br/>
        <label className='form-label' htmlFor='refSelector'>References :</label>
        <select id="refSelector" className="form-control mb-3" onChange={(e) => setSelectedRef(e.target.value)} value={finalRef} disabled={selectChildrens.length === 0}>
            {selectChildrens}
        </select>
        <div className='mb-3'>
            <div>
                <label htmlFor='refInfo' className='form-label'>{finalRef === "" ? "Aucune référence séléctionnée" : "Information sur " + finalRef}</label>
                <textarea id="refInfo" className="form-control" disabled={true} value={refInfo}/>
            </div>
            <div>
                <label htmlFor='refInfo' className='form-label'>Liste des appels</label>
                <textarea className="form-control" disabled={true} value={info.stack.join("\n")} />
            </div>
        </div>
    </>
}