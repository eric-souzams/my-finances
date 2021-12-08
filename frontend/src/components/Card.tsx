import { ReactNode } from "react";

type CardProps = {
    title: string;
    children?: ReactNode;
}

export function Card(props: CardProps) {
    return (
        <div className="card md-3">
            <h3 className="card-header">{props.title}</h3>
            <div className="card-body">
                {props.children}
            </div>
        </div>
    );
}